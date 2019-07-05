package com.coffeewithme.profileservice.serializers

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.springframework.data.mongodb.core.geo.GeoJsonPoint

class GeoJsonPointJacksonSerializer : JsonSerializer<GeoJsonPoint>() {
    override fun serialize(value: GeoJsonPoint?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        if (value != null) {
            gen?.writeArray(doubleArrayOf(value.x, value.y), 0, 2)
        }
    }

}

class GeoJsonPointJacksonDeSerializer : JsonDeserializer<GeoJsonPoint>() {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): GeoJsonPoint {
        val coordinates = p?.readValuesAs(DoubleArray::class.java)?.next()
        return GeoJsonPoint(coordinates!![0], coordinates[1])
    }

}
