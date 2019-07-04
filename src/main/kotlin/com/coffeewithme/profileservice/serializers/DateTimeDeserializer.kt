package com.coffeewithme.profileservice.serializers

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.time.Instant

class DateTimeDeserializer: JsonDeserializer<Instant>() {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): Instant {
        return Instant.parse(p?.valueAsString)
    }
}