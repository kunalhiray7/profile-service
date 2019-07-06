package com.coffeewithme.profileservice.domain

data class ImageMetadata(
        val fileName: String,
        val fileType: String,
        val correlationId: String
)