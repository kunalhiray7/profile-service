package com.coffeewithme.profileservice.services

import com.coffeewithme.profileservice.domain.Gender
import com.coffeewithme.profileservice.dtos.ProfileRequest
import com.coffeewithme.profileservice.exceptions.ProfileAlreadyExistsException
import com.coffeewithme.profileservice.repositories.ProfileRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import java.time.LocalDate

@RunWith(MockitoJUnitRunner::class)
internal class ProfileServiceTest {

    @Mock
    private lateinit var profileRepository: ProfileRepository

    @InjectMocks
    private lateinit var profileService: ProfileService

    @Test
    fun `create() should save profile and return saved profile`() {
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
        val domain = profileRequest.toDomain()
        val savedProfile = domain.copy(id = "5678tyuighjkbnm")
        doReturn(null).`when`(profileRepository).findByEmail(profileRequest.email)
        doReturn(savedProfile).`when`(profileRepository).save(domain)

        // when
        val result = profileService.create(profileRequest)

        // then
        assertEquals(savedProfile, result)
        verify(profileRepository, times(1)).findByEmail(profileRequest.email)
        verify(profileRepository, times(1)).save(domain)
        verifyNoMoreInteractions(profileRepository)
    }

    @Test
    fun `create() should throw ProfileAlreadyExistsException when tried to create profile with existing email`() {
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
        val domain = profileRequest.toDomain()
        doReturn(domain).`when`(profileRepository).findByEmail(profileRequest.email)

        // when
        try {
            profileService.create(profileRequest)
            fail()
        } catch (e: ProfileAlreadyExistsException) {
            assertEquals("Profile with email ${profileRequest.email} already exists.", e.message)
        }

        // then
        verify(profileRepository, times(1)).findByEmail(profileRequest.email)
        verifyNoMoreInteractions(profileRepository)
    }
}