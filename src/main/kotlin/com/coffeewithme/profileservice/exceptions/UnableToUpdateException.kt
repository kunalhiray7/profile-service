package com.coffeewithme.profileservice.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.CONFLICT)
class UnableToUpdateException(message: String) : Exception(message)
