package com.coffeewithme.profileservice.utils

import com.coffeewithme.profileservice.serializers.DateTimeDeserializer
import com.coffeewithme.profileservice.serializers.DateTimeSerializer
import com.coffeewithme.profileservice.serializers.GeoJsonPointJacksonDeSerializer
import com.coffeewithme.profileservice.serializers.GeoJsonPointJacksonSerializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import java.time.LocalDate

class ObjectMapperUtil {
    companion object {
        fun getObjectMapper(): ObjectMapper {
            val objectMapper = ObjectMapper()
            val javaTimeModule = JavaTimeModule()
            javaTimeModule.addSerializer(LocalDate::class.java, DateTimeSerializer())
            javaTimeModule.addDeserializer(LocalDate::class.java, DateTimeDeserializer())

            val module = SimpleModule()
            module.addSerializer(GeoJsonPoint::class.java, GeoJsonPointJacksonSerializer())
            module.addDeserializer(GeoJsonPoint::class.java, GeoJsonPointJacksonDeSerializer())

            objectMapper.registerModule(javaTimeModule)
            objectMapper.registerModule(KotlinModule())
            objectMapper.registerModule(module)

            return objectMapper
        }
    }
}