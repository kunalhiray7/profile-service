package com.coffeewithme.profileservice.controllers

import com.coffeewithme.profileservice.domain.Gender
import com.coffeewithme.profileservice.domain.Profile
import com.coffeewithme.profileservice.dtos.AuthRequest
import com.coffeewithme.profileservice.exceptions.NotAuthenticatedException
import com.coffeewithme.profileservice.services.ProfileService
import com.coffeewithme.profileservice.utils.ObjectMapperUtil
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
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
class AuthControllerTest {
    private lateinit var mockMvc: MockMvc

    @Mock
    private lateinit var profileService: ProfileService

    @InjectMocks
    private lateinit var authController: AuthController

    private val mapper = ObjectMapperUtil.getObjectMapper()

    @Before
    fun setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setMessageConverters(jacksonDateTimeConverter()).build()
    }

    private fun jacksonDateTimeConverter(): MappingJackson2HttpMessageConverter {
        val converter = MappingJackson2HttpMessageConverter()
        converter.objectMapper = this.mapper
        return converter
    }

    @Test
    fun `PUT should authenticate user and return profile`() {
        // given
        val email = "john.smith@gmail.com"
        val profile = Profile(
                id = "5d1e4a25ba52df864cc09028",
                email = email,
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
        val authRequest = AuthRequest(username = email)
        Mockito.doReturn(profile).`when`(profileService).authenticate(authRequest)

        // when
        mockMvc.perform(
                MockMvcRequestBuilders.put("/authentications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(authRequest))
        ) // then
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().string(mapper.writeValueAsString(profile)))

        Mockito.verify(profileService, Mockito.times(1)).authenticate(authRequest)
    }

    @Test
    fun `PUT should return 401 when user is not authenticated`() {
        // given
        val email = "john.smith@gmail.com"
        val authRequest = AuthRequest(username = email)
        Mockito.doThrow(NotAuthenticatedException("User is not authorized")).`when`(profileService).authenticate(authRequest)

        // when
        mockMvc.perform(
                MockMvcRequestBuilders.put("/authentications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(authRequest))
        ) // then
                .andExpect(MockMvcResultMatchers.status().isUnauthorized)

        Mockito.verify(profileService, Mockito.times(1)).authenticate(authRequest)
    }

}