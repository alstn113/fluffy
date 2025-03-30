package com.fluffy.exam.application.response

import com.fluffy.exam.domain.dto.AuthorDto
import java.time.LocalDateTime

data class ExamDetailSummaryResponse(
    val id: Long,
    val title: String,
    val description: String,
    val status: String,
    val author: AuthorDto,
    val questionCount: Long,
    val likeCount: Long,
    val isLiked: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
