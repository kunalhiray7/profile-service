package com.coffeewithme.profileservice.controllers

import com.coffeewithme.profileservice.domain.Gender
import com.coffeewithme.profileservice.domain.Profile
import com.coffeewithme.profileservice.dtos.ProfileRequest
import com.coffeewithme.profileservice.exceptions.ProfileAlreadyExistsException
import com.coffeewithme.profileservice.exceptions.ProfileNotFoundException
import com.coffeewithme.profileservice.services.ProfileService
import com.coffeewithme.profileservice.utils.ObjectMapperUtil.Companion.getObjectMapper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.time.LocalDate

@RunWith(MockitoJUnitRunner::class)
internal class ProfileControllerTest {

    private lateinit var mockMvc: MockMvc

    @Mock
    private lateinit var profileService: ProfileService

    @InjectMocks
    private lateinit var profileController: ProfileController

    private val mapper = getObjectMapper()

    @Before
    fun setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(profileController)
                .setMessageConverters(jacksonDateTimeConverter()).build()
    }

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
                MockMvcRequestBuilders.post("/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(profileRequest))
        ) // then
                .andExpect(MockMvcResultMatchers.status().isCreated)
                .andExpect(MockMvcResultMatchers.content().string(mapper.writeValueAsString(domainProfile)))

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
                MockMvcRequestBuilders.post("/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(profileRequest))
        ) // then
                .andExpect(MockMvcResultMatchers.status().isConflict)

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
        mockMvc.perform(MockMvcRequestBuilders.get("/profiles/$id"))
                // then
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().string(mapper.writeValueAsString(profile)))

        verify(profileService, times(1)).getById(id)
        verifyNoMoreInteractions(profileService)
    }

    @Test
    fun `GET by id should return 404 when no profile found for given id`() {
        // given
        val id = "5d1e4a25ba52df864cc09028"
        doThrow(ProfileNotFoundException("Profile not found")).`when`(profileService).getById(id)

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/profiles/$id"))
                // then
                .andExpect(MockMvcResultMatchers.status().isNotFound)

        verify(profileService, times(1)).getById(id)
        verifyNoMoreInteractions(profileService)
    }

    private fun jacksonDateTimeConverter(): MappingJackson2HttpMessageConverter {
        val converter = MappingJackson2HttpMessageConverter()
        converter.objectMapper = this.mapper
        return converter
    }

}