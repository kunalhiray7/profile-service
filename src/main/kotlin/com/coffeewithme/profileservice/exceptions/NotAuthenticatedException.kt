package com.coffeewithme.profileservice.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.Exception

@ResponseStatus(HttpStatus.UNAUTHORIZED)
class NotAuthenticatedException(message: String): Exception(message)
