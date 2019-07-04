package com.coffeewithme.profileservice.services

import com.coffeewithme.profileservice.domain.Gender
import com.coffeewithme.profileservice.dtos.ProfileRequest
import com.coffeewithme.profileservice.repositories.ProfileRepository
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import java.time.LocalDate

@RunWith(MockitoJUnitRunner::class)
internal class ProfileServiceTest {

    @Mock
    private lateinit var profileRepository: ProfileRepository

    @InjectMocks
    private lateinit var profileService: ProfileService

    @Test
    fun `should save profile and return saved profile`() {
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
        val domain = profileRequest.toDomain()
        val savedProfile = domain.copy(id = "5678tyuighjkbnm")
        doReturn(savedProfile).`when`(profileRepository).save(domain)

        // when
        val result = profileService.create(profileRequest)

        // then
        assertEquals(savedProfile, result)
        verify(profileRepository, times(1)).save(domain)
    }
}