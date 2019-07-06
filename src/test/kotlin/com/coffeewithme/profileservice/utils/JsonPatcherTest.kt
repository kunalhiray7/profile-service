package com.coffeewithme.profileservice.utils

import com.coffeewithme.profileservice.domain.Gender
import com.coffeewithme.profileservice.domain.Profile
import com.coffeewithme.profileservice.exceptions.UnprocessableEntityException
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import java.time.LocalDate

class JsonPatcherTest {

    private val objectMapper = ObjectMapperUtil.getObjectMapper()

    private lateinit var jsonPatcher: JsonPatcher

    @Before
    fun setUp() {
        jsonPatcher = JsonPatcher(objectMapper)
    }

    @Test
    fun `should apply the patch and return modified object`() {
        val displayName = "John Snow"
        val realName = "Aegon Targaryen"
        val patchRequest = """[{ "op": "replace", "path": "/displayName", "value": "$displayName" },
            |{ "op": "replace", "path": "/realName", "value": "$realName" }]""".trimMargin()
        val profile = Profile(
                id = "5d1e4a25ba52df864cc09028",
                email = "john.snow@gmail.com",
                realName = "John Snow",
                displayName = "John",
                gender = Gender.MALE,
                dateOfBirth = LocalDate.now(),
                maritalStatus = "Single",
                profilePic = "http://123.png",
                ethnicity = "Asian",
                religion = "Christian",
                height = 173,
                figure = "Normal",
                occupation = "Banker",
                aboutMe = "Banker by profession, musician by passion",
                city = "Berlin",
                location = GeoJsonPoint(52.46510, 13.39630)
        )

        val result = jsonPatcher.patch(patchRequest, profile)

        assertEquals(profile.copy(displayName = displayName, realName = realName), result)
    }

    @Test
    fun `should throw UnprocessableEntityException when patch cannot be applied`() {
        val profile = Profile(
                id = "5d1e4a25ba52df864cc09028",
                email = "john.snow@gmail.com",
                realName = "John Snow",
                displayName = "John",
                gender = Gender.MALE,
                dateOfBirth = LocalDate.now(),
                maritalStatus = "Single",
                profilePic = "http://123.png",
                ethnicity = "Asian",
                religion = "Christian",
                height = 173,
                figure = "Normal",
                occupation = "Banker",
                aboutMe = "Banker by profession, musician by passion",
                city = "Berlin",
                location = GeoJsonPoint(52.46510, 13.39630)
        )

        try {
            jsonPatcher.patch("invalid Input", profile)
            fail()
        } catch (e: UnprocessableEntityException) {
            assertEquals("Request body is invalid and cannot be processed: Unrecognized token 'invalid': was expecting ('true', 'false' or 'null')\n" +
                    " at [Source: (String)\"invalid Input\"; line: 1, column: 8]", e.message)
        }
    }
}