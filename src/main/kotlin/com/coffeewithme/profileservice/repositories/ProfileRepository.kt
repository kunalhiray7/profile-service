package com.coffeewithme.profileservice.repositories

import com.coffeewithme.profileservice.domain.Profile
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ProfileRepository: MongoRepository<Profile, String>