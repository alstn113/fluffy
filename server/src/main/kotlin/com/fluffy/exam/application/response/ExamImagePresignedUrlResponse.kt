package com.fluffy.exam.application.response

data class ExamImagePresignedUrlResponse(
    val presignedUrl: String,
    val imageUrl: String
)
