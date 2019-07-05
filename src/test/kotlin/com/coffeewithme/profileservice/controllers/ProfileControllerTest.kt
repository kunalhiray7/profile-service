package com.coffeewithme.profileservice.controllers

import com.coffeewithme.profileservice.domain.Gender
import com.coffeewithme.profileservice.domain.Profile
import com.coffeewithme.profileservice.dtos.ProfileRequest
import com.coffeewithme.profileservice.exceptions.ProfileAlreadyExistsException
import com.coffeewithme.profileservice.exceptions.ProfileNotFoundException
import com.coffeewithme.profileservice.exceptions.UnableToUpdateException
import com.coffeewithme.profileservice.services.ProfileService
import com.coffeewithme.profileservice.utils.ObjectMapperUtil.Companion.getObjectMapper
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate

@RunWith(SpringRunner::class)
@WebMvcTest(ProfileController::class)
internal class ProfileControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var profileService: ProfileService

    private val mapper = getObjectMapper()

    @Test
    fun `POST should create a profile and return 201`() {
        // given
        val profileRequest = ProfileRequest(
                email = "john.smith@gmail.com",
                realName = "John Smith",
                displayName = "John",
                gender = Gender.MALE,
                dateOfBirth = LocalDate.now(),
                maritalStatus = "Single",
                profilePic = "http://123.png",
                ethnicity = "Asian",
                religion = "Christian",
                height = 173,
                figure = "Normal",
                occupation = "Banker",
                aboutMe = "Banker by profession, musician by passion",
                city = "Berlin",
                location = GeoJsonPoint(52.46510, 13.39630)
        )
        val domainProfile = profileRequest.toDomain()
        doReturn(domainProfile).`when`(profileService).create(profileRequest)

        // when
        mockMvc.perform(
                post("/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(profileRequest))
        ) // then
                .andExpect(status().isCreated)
                .andExpect(content().string(mapper.writeValueAsString(domainProfile)))

        verify(profileService, times(1)).create(profileRequest)

    }

    @Test
    fun `POST should return 409 when tried to create profile with already existing user`() {
        // given
        val profileRequest = ProfileRequest(
                email = "john.smith@gmail.com",
                realName = "John Smith",
                displayName = "John",
                gender = Gender.MALE,
                dateOfBirth = LocalDate.now(),
                maritalStatus = "Single",
                profilePic = "http://123.png",
                ethnicity = "Asian",
                religion = "Christian",
                height = 173,
                figure = "Normal",
                occupation = "Banker",
                aboutMe = "Banker by profession, musician by passion",
                city = "Berlin",
                location = GeoJsonPoint(52.46510, 13.39630)
        )
        doThrow(ProfileAlreadyExistsException("Profile with this email already exists")).`when`(profileService).create(profileRequest)

        // when
        mockMvc.perform(
                post("/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(profileRequest))
        ) // then
                .andExpect(status().isConflict)

        verify(profileService, times(1)).create(profileRequest)
        verifyNoMoreInteractions(profileService)
    }

    @Test
    fun `GET by id should return profile for given id`() {
        // given
        val id = "5d1e4a25ba52df864cc09028"
        val profile = Profile(
                id = id,
                email = "john.smith@gmail.com",
                realName = "John Smith",
                displayName = "John",
                gender = Gender.MALE,
                dateOfBirth = LocalDate.now(),
                maritalStatus = "Single",
                profilePic = "http://123.png",
                ethnicity = "Asian",
                religion = "Christian",
                height = 173,
                figure = "Normal",
                occupation = "Banker",
                aboutMe = "Banker by profession, musician by passion",
                city = "Berlin",
                location = GeoJsonPoint(52.46510, 13.39630)
        )
        doReturn(profile).`when`(profileService).getById(id)

        // when
        mockMvc.perform(get("/profiles/$id"))
                // then
                .andExpect(status().isOk)
                .andExpect(content().string(mapper.writeValueAsString(profile)))

        verify(profileService, times(1)).getById(id)
        verifyNoMoreInteractions(profileService)
    }

    @Test
    fun `GET by id should return 404 when no profile found for given id`() {
        // given
        val id = "5d1e4a25ba52df864cc09028"
        doThrow(ProfileNotFoundException("Profile not found")).`when`(profileService).getById(id)

        // when
        mockMvc.perform(get("/profiles/$id"))
                // then
                .andExpect(status().isNotFound)

        verify(profileService, times(1)).getById(id)
        verifyNoMoreInteractions(profileService)
    }

    @Test
    fun `PATCH should return updated profile`() {
        // given
        val patchRequest = """[{ "op": "replace", "path": "/displayName", "value": "John Snow" },
            |{ "op": "replace", "path": "/realName", "value": "Aegon Targeryen" }]""".trimMargin()

        val id = "5d1e4a25ba52df864cc09028"
        val updatedProfile = Profile(
                id = id,
                email = "john.snow@gmail.com",
                realName = "Aegon Targaryen",
                displayName = "John Snow",
                gender = Gender.MALE,
                dateOfBirth = LocalDate.now(),
                maritalStatus = "Single",
                profilePic = "http://123.png",
                ethnicity = "Asian",
                religion = "Christian",
                height = 173,
                figure = "Normal",
                occupation = "Banker",
                aboutMe = "Banker by profession, musician by passion",
                city = "Berlin",
                location = GeoJsonPoint(52.46510, 13.39630)
        )
        doReturn(updatedProfile).`when`(profileService).update(patchRequest, id)

        // when
        mockMvc.perform(
                patch("/profiles/$id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchRequest))
                .andExpect(status().isOk)
                .andExpect(content().string(mapper.writeValueAsString(updatedProfile)))

        verify(profileService, times(1)).update(patchRequest, id)
        verifyNoMoreInteractions(profileService)
    }

    @Test
    fun `PATCH should return 404 when profile not found for given id`() {
        // given
        val id = "5d1e4a25ba52df864cc09028"
        val patchRequest = """[{ "op": "replace", "path": "/displayName", "value": "John Snow" },
            |{ "op": "replace", "path": "/realName", "value": "Aegon Targeryen" }]""".trimMargin()
        doThrow(ProfileNotFoundException("Profile not found")).`when`(profileService).update(patchRequest, id)

        // when
        mockMvc.perform(
                patch("/profiles/$id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchRequest))
                .andExpect(status().isNotFound)

        verify(profileService, times(1)).update(patchRequest, id)
        verifyNoMoreInteractions(profileService)
    }

    @Test
    fun `PATCH should return 409 when profile cannot be updated`() {
        // given
        val id = "5d1e4a25ba52df864cc09028"
        val patchRequest = """[{ "op": "replace", "path": "/id", "value": "5d1e4a25ba52df864cc09065" },
            |{ "op": "replace", "path": "/email", "value": "aagon@Targeryen.com" }]""".trimMargin()
        doThrow(UnableToUpdateException("Profile cannot be updated")).`when`(profileService).update(patchRequest, id)

        // when
        mockMvc.perform(
                patch("/profiles/$id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchRequest))
                .andExpect(status().isConflict)

        verify(profileService, times(1)).update(patchRequest, id)
        verifyNoMoreInteractions(profileService)
    }

}