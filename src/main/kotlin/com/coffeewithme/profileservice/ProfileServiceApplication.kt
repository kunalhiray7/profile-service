package com.coffeewithme.profileservice

import com.coffeewithme.profileservice.utils.ObjectMapperUtil.Companion.getObjectMapper
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import springfox.documentation.swagger2.annotations.EnableSwagger2

@SpringBootApplication
@EnableSwagger2
class ProfileServiceApplication {
    @Bean
    @Primary
    fun serializingObjectMapper(): ObjectMapper {
        return getObjectMapper()
    }
}

fun main(args: Array<String>) {
    runApplication<ProfileServiceApplication>(*args)
}
