package com.fluffy.exam.domain.dto

import com.fluffy.exam.domain.ExamStatus
import java.time.LocalDateTime

data class MyExamSummaryDto(
    val id: Long,
    val title: String,
    val description: String,
    val status: ExamStatus,
    val author: AuthorDto,
    val questionCount: Long,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
