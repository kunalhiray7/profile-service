package com.coffeewithme.profileservice.services

import com.coffeewithme.profileservice.domain.Profile
import com.coffeewithme.profileservice.dtos.AuthRequest
import com.coffeewithme.profileservice.dtos.ProfileRequest
import com.coffeewithme.profileservice.exceptions.NotAuthenticatedException
import com.coffeewithme.profileservice.exceptions.ProfileAlreadyExistsException
import com.coffeewithme.profileservice.exceptions.ProfileNotFoundException
import com.coffeewithme.profileservice.exceptions.UnableToUpdateException
import com.coffeewithme.profileservice.repositories.ProfileRepository
import com.coffeewithme.profileservice.utils.JsonPatcher
import org.springframework.stereotype.Service

@Service
class ProfileService(private val profileRepository: ProfileRepository, private val jsonPatcher: JsonPatcher) {

    @Throws(ProfileAlreadyExistsException::class)
    fun create(profileRequest: ProfileRequest): Profile {
        validateProfile(profileRequest)
        return profileRepository.save(profileRequest.toDomain())
    }

    @Throws(ProfileNotFoundException::class)
    fun getById(id: String): Profile = getOrThrowNotFound(id)

    @Throws(NotAuthenticatedException::class)
    fun authenticate(authRequest: AuthRequest): Profile = profileRepository.findByEmail(authRequest.username)
                ?: throw NotAuthenticatedException("User with username ${authRequest.username} is not authorized.")

    @Throws(ProfileNotFoundException::class, UnableToUpdateException::class)
    fun update(patchRequest: String, id: String): Profile {
        val existingProfile = getOrThrowNotFound(id)

        val updatedProfile = jsonPatcher.patch(patchRequest, existingProfile)
        checkAllowedOperations(updatedProfile, existingProfile)

        return profileRepository.save(updatedProfile)
    }

    fun getAll(): List<Profile> = profileRepository.findAll()

    private fun checkAllowedOperations(updatedProfile: Profile, existingProfile: Profile) {
        when(updatedProfile.id != existingProfile.id
                || updatedProfile.email != existingProfile.email
                || updatedProfile.height != existingProfile.height) {
            true -> throw UnableToUpdateException("Email and ID are not allowed to be updated")
            false -> return
        }
    }

    private fun validateProfile(profileRequest: ProfileRequest) {
        val potentialProfile = profileRepository.findByEmail(profileRequest.email)
        if (potentialProfile != null) {
            throw ProfileAlreadyExistsException("Profile with email ${profileRequest.email} already exists.")
        }
    }

    private fun getOrThrowNotFound(id: String): Profile {
        return profileRepository.findById(id).orElseThrow { ProfileNotFoundException("Profile with id $id does not exist.") }
    }
}