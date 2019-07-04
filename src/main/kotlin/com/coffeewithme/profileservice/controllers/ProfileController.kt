package com.coffeewithme.profileservice.controllers

import com.coffeewithme.profileservice.dtos.ProfileRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/profiles")
class ProfileController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody @Valid profileRequest: ProfileRequest): ProfileRequest{
        return profileRequest
    }
}