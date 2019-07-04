package com.coffeewithme.profileservice.controllers

import com.coffeewithme.profileservice.domain.Gender
import com.coffeewithme.profileservice.dtos.ProfileRequest
import com.coffeewithme.profileservice.serializers.DateTimeDeserializer
import com.coffeewithme.profileservice.serializers.DateTimeSerializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import java.time.Instant

@RunWith(MockitoJUnitRunner::class)
internal class ProfileControllerTest {

    private lateinit var mockMvc: MockMvc

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
                dateOfBirth = Instant.now(),
                maritalStatus = "Single",
                profilePic = "http://123.png",
                ethnicity = "Asian",
                religion = "Christian",
                height = 173,
                figure = "Normal",
                occupation = "Banker",
                aboutMe = "Banker by profession, musician by passion"
        )

        // when
        mockMvc.perform(
                MockMvcRequestBuilders.post("/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(profileRequest))
        ) // then
                .andExpect(MockMvcResultMatchers.status().isCreated)
                .andExpect(MockMvcResultMatchers.content().string(mapper.writeValueAsString(profileRequest)))

    }

    private fun getObjectMapper(): ObjectMapper {
        val objectMapper = ObjectMapper()
        val javaTimeModule = JavaTimeModule()
        javaTimeModule.addSerializer(Instant::class.java, DateTimeSerializer())
        javaTimeModule.addDeserializer(Instant::class.java, DateTimeDeserializer())
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