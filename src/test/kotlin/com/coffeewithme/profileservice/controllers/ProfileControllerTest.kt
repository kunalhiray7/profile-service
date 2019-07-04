package com.coffeewithme.profileservice.controllers

import com.coffeewithme.profileservice.domain.Gender
import com.coffeewithme.profileservice.dtos.ProfileRequest
import com.coffeewithme.profileservice.serializers.DateTimeDeserializer
import com.coffeewithme.profileservice.serializers.DateTimeSerializer
import com.coffeewithme.profileservice.services.ProfileService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
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
                aboutMe = "Banker by profession, musician by passion"
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

    private fun getObjectMapper(): ObjectMapper {
        val objectMapper = ObjectMapper()
        val javaTimeModule = JavaTimeModule()
        javaTimeModule.addSerializer(LocalDate::class.java, DateTimeSerializer())
        javaTimeModule.addDeserializer(LocalDate::class.java, DateTimeDeserializer())
        objectMapper.registerModule(javaTimeModule)
        objectMapper.registerModule(KotlinModule())

        return objectMapper
    }

    private fun jacksonDateTimeConverter(): MappingJackson2HttpMessageConverter {
        val converter = MappingJackson2HttpMessageConverter()
        converter.objectMapper = getObjectMapper()
        return converter
    }

}