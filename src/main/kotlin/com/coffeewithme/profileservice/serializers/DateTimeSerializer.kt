package com.coffeewithme.profileservice.serializers

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.time.Instant

class DateTimeSerializer: JsonSerializer<Instant>() {
    override fun serialize(value: Instant?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        gen?.writeString(Instant.parse(value?.toString()).toString())
    }
}