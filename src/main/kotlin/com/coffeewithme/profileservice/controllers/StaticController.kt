package com.coffeewithme.profileservice.controllers

import com.mongodb.util.JSON
import org.springframework.http.MediaType
import org.springframework.util.ResourceUtils
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.nio.file.Files


@CrossOrigin
@RestController
class StaticController {

    @GetMapping("/cities", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getCities(): Any? {
        return getJson("classpath:cities.json")
    }

    @GetMapping("/single_choice_attributes", produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getSingleChoiceAttributes(): Any? {
        return getJson("classpath:single_choice_attributes.json")
    }

    fun getJson(path: String): Any? {
        val file: File?
        try {
            file = ResourceUtils.getFile(path)
            val content = String(Files.readAllBytes(file.toPath()))
            return JSON.parse(content)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }
}