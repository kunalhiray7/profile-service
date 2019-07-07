package com.coffeewithme.profileservice.dtos

import com.coffeewithme.profileservice.domain.Gender
import com.coffeewithme.profileservice.domain.Profile
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import java.time.LocalDate
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class ProfileRequest(

        @get: NotBlank
        val email: String = "",

        @get: NotBlank
        @get: Size(min = 1, max = 256)
        val realName: String = "",

        @get: NotBlank
        @get: Size(min = 1, max = 256)
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

        @get: Size(min = 0, max = 256)
        val occupation: String = "",

        @get: Size(min = 0, max = 5000)
        val aboutMe: String = "",

        @get: NotBlank
        val city: String = "",

        @get: NotNull
        val location: GeoJsonPoint = GeoJsonPoint(52.46510, 13.39630)

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
            aboutMe = this.aboutMe,
            city = this.city,
            location = this.location
    )
}
