package com.fluffy.global.storage.response

data class PresignedUrlResponse(
    val presignedUrl: String,
    val fileUrl: String,
)