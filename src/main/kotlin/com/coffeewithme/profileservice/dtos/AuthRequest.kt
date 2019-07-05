package com.coffeewithme.profileservice.dtos

import javax.validation.constraints.NotBlank

data class AuthRequest(
        @get: NotBlank
        val username: String
)