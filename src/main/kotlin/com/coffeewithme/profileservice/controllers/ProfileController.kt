package com.coffeewithme.profileservice.controllers

import com.coffeewithme.profileservice.domain.Profile
import com.coffeewithme.profileservice.dtos.ProfileRequest
import com.coffeewithme.profileservice.services.ProfileService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/profiles")
class ProfileController(val profileService: ProfileService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody @Valid profileRequest: ProfileRequest): Profile = profileService.create(profileRequest)
}