package com.coffeewithme.profileservice.services

import com.coffeewithme.profileservice.domain.Profile
import com.coffeewithme.profileservice.dtos.ProfileRequest
import com.coffeewithme.profileservice.exceptions.ProfileAlreadyExistsException
import com.coffeewithme.profileservice.repositories.ProfileRepository
import org.springframework.stereotype.Service

@Service
class ProfileService(private val profileRepository: ProfileRepository) {

    @Throws(ProfileAlreadyExistsException::class)
    fun create(profileRequest: ProfileRequest): Profile {
        validateProfile(profileRequest)
        return profileRepository.save(profileRequest.toDomain())
    }

    private fun validateProfile(profileRequest: ProfileRequest) {
        val potentialProfile = profileRepository.findByEmail(profileRequest.email)
        if(potentialProfile != null) {
            throw ProfileAlreadyExistsException("Profile with email ${profileRequest.email} already exists.")
        }
    }
}