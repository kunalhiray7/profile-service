package com.coffeewithme.profileservice.controllers

import com.coffeewithme.profileservice.domain.Profile
import com.coffeewithme.profileservice.dtos.AuthRequest
import com.coffeewithme.profileservice.services.ProfileService
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/authentications")
class AuthController(val profileService: ProfileService) {

    @PutMapping
    fun authenticate(@RequestBody @Valid authRequest: AuthRequest): Profile = profileService.authenticate(authRequest)
}