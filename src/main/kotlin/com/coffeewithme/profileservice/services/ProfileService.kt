package com.coffeewithme.profileservice.services

import com.coffeewithme.profileservice.domain.Profile
import com.coffeewithme.profileservice.dtos.ProfileRequest
import com.coffeewithme.profileservice.repositories.ProfileRepository
import org.springframework.stereotype.Service

@Service
class ProfileService(private val profileRepository: ProfileRepository) {
    fun create(profileRequest: ProfileRequest): Profile {
        return profileRepository.save(profileRequest.toDomain())
    }
}