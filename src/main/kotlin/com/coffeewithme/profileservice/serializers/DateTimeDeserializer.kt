package com.coffeewithme.profileservice.serializers

import com.coffeewithme.profileservice.constants.Constants
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.time.LocalDate

class DateTimeDeserializer : JsonDeserializer<LocalDate>() {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): LocalDate {
        return LocalDate.parse(p?.valueAsString, Constants.FORMATTER)
    }
}