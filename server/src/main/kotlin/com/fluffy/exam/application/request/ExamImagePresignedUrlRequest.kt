package com.fluffy.exam.application.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class ExamImagePresignedUrlRequest(
    @field:NotNull @field:NotBlank val imageName: String,
    val fileSize: Long
)
