package com.coffeewithme.profileservice.constants

import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class Constants {
    companion object {
        val FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneOffset.UTC)
    }
}