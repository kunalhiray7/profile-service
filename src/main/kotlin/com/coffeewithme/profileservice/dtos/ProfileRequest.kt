package com.coffeewithme.profileservice.dtos

import com.coffeewithme.profileservice.domain.Gender
import java.time.Instant
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class ProfileRequest(

        @get: NotBlank
        val email: String = "",

        @get: NotBlank
        val realName: String = "",

        @get: NotBlank
        val displayName: String = "",

        @get: NotNull
        val gender: Gender = Gender.MALE,

        @get: NotNull
        val dateOfBirth: Instant = Instant.now(),

        @get: NotBlank
        val maritalStatus: String = "",

        val profilePic: String = "",

        val ethnicity: String = "",

        val religion: String = "",

        val height: Int = 0,

        val figure: String = "",

        val occupation: String = "",

        val aboutMe: String = ""

)
