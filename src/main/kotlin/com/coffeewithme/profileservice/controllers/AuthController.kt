package com.coffeewithme.profileservice.controllers

import com.coffeewithme.profileservice.domain.Profile
import com.coffeewithme.profileservice.dtos.AuthRequest
import com.coffeewithme.profileservice.services.ProfileService
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@CrossOrigin
@RestController
@RequestMapping("/authentications")
class AuthController(val profileService: ProfileService) {

    @PutMapping
    fun authenticate(@RequestBody @Valid authRequest: AuthRequest): Profile = profileService.authenticate(authRequest)
}