package com.coffeewithme.profileservice.controllers

import com.coffeewithme.profileservice.services.ImageService
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.util.UriComponentsBuilder
import java.util.*

@CrossOrigin(exposedHeaders = ["Location"])
@RestController
@RequestMapping("/images")
class ImageController(private val imageService: ImageService) {

    @PostMapping(consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @ResponseStatus(HttpStatus.CREATED)
    fun upload(@RequestBody @RequestParam("file") file: MultipartFile, uriComponentsBuilder: UriComponentsBuilder): ResponseEntity<String> {
        val imageId = imageService.saveFile(file.inputStream, file.originalFilename ?: UUID.randomUUID().toString() + ".png", file.contentType ?: "image/png")
        val location = uriComponentsBuilder.path("/images/{imageId}").buildAndExpand(imageId).toUri()
        return ResponseEntity.created(location).body(imageId)
    }

    @GetMapping("/{imageId}")
    fun get(@PathVariable("imageId") imageId: String): ResponseEntity<InputStreamResource> =
            ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imageService.get(imageId))
}