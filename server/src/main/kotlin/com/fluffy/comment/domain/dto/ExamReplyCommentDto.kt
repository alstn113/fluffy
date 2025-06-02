package com.fluffy.comment.domain.dto

import java.time.LocalDateTime

data class ExamReplyCommentDto(
    val id: Long,
    val content: String,
    val author: AuthorDto,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)