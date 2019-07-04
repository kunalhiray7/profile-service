package com.coffeewithme.profileservice

import com.coffeewithme.profileservice.serializers.DateTimeDeserializer
import com.coffeewithme.profileservice.serializers.DateTimeSerializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import springfox.documentation.swagger2.annotations.EnableSwagger2
import java.time.LocalDate

@SpringBootApplication
@EnableSwagger2
class ProfileServiceApplication {
    @Bean
    @Primary
    fun serializingObjectMapper(): ObjectMapper {
        return getObjectMapper()
    }

    private fun getObjectMapper(): ObjectMapper {
        val objectMapper = ObjectMapper()
        val javaTimeModule = JavaTimeModule()
        javaTimeModule.addSerializer(LocalDate::class.java, DateTimeSerializer())
        javaTimeModule.addDeserializer(LocalDate::class.java, DateTimeDeserializer())
        objectMapper.registerModule(javaTimeModule)
        objectMapper.registerModule(KotlinModule())

        return objectMapper
    }
}

fun main(args: Array<String>) {
    runApplication<ProfileServiceApplication>(*args)
}
