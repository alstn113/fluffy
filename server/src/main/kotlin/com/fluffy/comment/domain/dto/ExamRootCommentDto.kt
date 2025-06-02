package com.fluffy.comment.domain.dto

import java.time.LocalDateTime

data class ExamRootCommentDto(
    val id: Long,
    val content: String,
    val author: AuthorDto,
    val replyCount: Long,
    val isDeleted: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {

    fun asDeleted(): ExamRootCommentDto {
        return ExamRootCommentDto(
            id,
            "",
            author.asDeleted(),
            replyCount,
            true,
            LocalDateTime.MIN,
            LocalDateTime.MIN,
        )
    }
}
