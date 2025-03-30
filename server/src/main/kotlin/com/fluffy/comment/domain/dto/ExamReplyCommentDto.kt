package com.fluffy.comment.domain.dto

import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDateTime

data class ExamReplyCommentDto @QueryProjection constructor(
    val id: Long,
    val content: String,
    val author: AuthorDto,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)