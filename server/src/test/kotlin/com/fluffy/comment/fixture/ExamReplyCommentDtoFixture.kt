package com.fluffy.comment.fixture

import com.fluffy.comment.domain.dto.AuthorDto
import com.fluffy.comment.domain.dto.ExamReplyCommentDto
import java.time.LocalDateTime

object ExamReplyCommentDtoFixture {

    fun create(
        id: Long = 1L,
        content: String = "답글입니다.",
        author: AuthorDto = AuthorDto(
            id = 1L,
            name = "example",
            avatarUrl = "https://example.com/avatar.jpg",
        ),
    ): ExamReplyCommentDto {
        return ExamReplyCommentDto(
            id = id,
            content = content,
            author = author,
            createdAt = LocalDateTime.MIN,
            updatedAt = LocalDateTime.MIN,
        )
    }
}