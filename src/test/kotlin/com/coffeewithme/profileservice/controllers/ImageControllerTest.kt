package com.coffeewithme.profileservice.controllers

import com.coffeewithme.profileservice.exceptions.ProfileNotFoundException
import com.coffeewithme.profileservice.services.ImageService
import com.mongodb.client.gridfs.model.GridFSFile
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.times
import org.bson.BsonObjectId
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.core.io.ResourceLoader
import org.springframework.data.mongodb.gridfs.GridFsResource
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.util.*

@RunWith(SpringRunner::class)
@WebMvcTest(ImageController::class)
class ImageControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var resourceLoader: ResourceLoader

    @MockBean
    private lateinit var imageService: ImageService

    @Test
    fun `POST should upload image`() {
        val fileName = "dummy_image.jpg"
        val contentType = "image/png"
        val file = resourceLoader.getResource("classpath:$fileName")
        val multipartFile = MockMultipartFile("file", fileName, contentType, file.inputStream)

        val imageId = "5d1e4a25ba52df864cc09028"
        doReturn(imageId).`when`(imageService).saveFile(any(), any(), any())

        mockMvc.perform(
                multipart("/images")
                        .file(multipartFile))
                .andExpect(header().string("Location", "http://localhost/images/$imageId"))
                .andExpect(content().string(imageId))
                .andExpect(status().isCreated)

        verify(imageService, times(1)).saveFile(any(), any(), any())
        verifyNoMoreInteractions(imageService)
    }

    @Test
    fun `GET should download image`() {
        val imageId = "5d1e4a25ba52df864cc09028"
        val fileName = "dummy_image.jpg"
        val gridFsResource = GridFsResource(GridFSFile(BsonObjectId(), fileName, 345L, 3, Date(), null, null, null))

        doReturn(gridFsResource).`when`(imageService).get(imageId)

        mockMvc.perform(get("/images/$imageId"))
                .andExpect(status().isOk)
                .andExpect(header().string("Content-Type", MediaType.IMAGE_PNG_VALUE))

        verify(imageService, times(1)).get(imageId)
        verifyNoMoreInteractions(imageService)
    }

    @Test
    fun `GET should return 404 when no image found for given id`() {
        val imageId = "5d1e4a25ba52df864cc09028"
        doThrow(ProfileNotFoundException("No Image found for given id")).`when`(imageService).get(imageId)

        mockMvc.perform(get("/images/$imageId"))
                .andExpect(status().isNotFound)

        verify(imageService, times(1)).get(imageId)
        verifyNoMoreInteractions(imageService)
    }
}