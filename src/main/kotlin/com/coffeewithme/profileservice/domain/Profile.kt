package com.coffeewithme.profileservice.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Document(collection = "profiles")
data class Profile(
        @Id
        val id: String?,

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
        val location: GeoJsonPoint
)