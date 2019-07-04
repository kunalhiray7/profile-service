package com.coffeewithme.profileservice.dtos

import com.coffeewithme.profileservice.domain.Gender
import com.coffeewithme.profileservice.domain.Profile
import java.time.LocalDate
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
        val dateOfBirth: LocalDate = LocalDate.now(),

        @get: NotBlank
        val maritalStatus: String = "",

        val profilePic: String = "",

        val ethnicity: String = "",

        val religion: String = "",

        val height: Int = 0,

        val figure: String = "",

        val occupation: String = "",

        val aboutMe: String = ""

) {
    fun toDomain() = Profile(
            id = null,
            email = this.email,
            realName = this.realName,
            displayName = this.displayName,
            gender = this.gender,
            dateOfBirth = this.dateOfBirth,
            maritalStatus = this.maritalStatus,
            profilePic = this.profilePic,
            ethnicity = this.ethnicity,
            religion = this.religion,
            height = this.height,
            figure = this.figure,
            occupation = this.occupation,
            aboutMe = this.aboutMe
    )
}
