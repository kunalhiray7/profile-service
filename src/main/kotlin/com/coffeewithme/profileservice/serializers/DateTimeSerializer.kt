package com.coffeewithme.profileservice.serializers

import com.coffeewithme.profileservice.constants.Constants
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.time.LocalDate

class DateTimeSerializer: JsonSerializer<LocalDate>() {
    override fun serialize(value: LocalDate?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        gen?.writeString(value?.format(Constants.FORMATTER))
    }
}