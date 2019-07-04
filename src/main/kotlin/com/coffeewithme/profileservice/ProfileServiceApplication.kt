package com.coffeewithme.profileservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import springfox.documentation.swagger2.annotations.EnableSwagger2

@SpringBootApplication
@EnableSwagger2
class ProfileServiceApplication

fun main(args: Array<String>) {
	runApplication<ProfileServiceApplication>(*args)
}
