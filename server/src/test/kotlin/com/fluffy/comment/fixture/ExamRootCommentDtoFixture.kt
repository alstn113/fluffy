package com.fluffy.comment.fixture

import com.fluffy.comment.domain.dto.AuthorDto
import com.fluffy.comment.domain.dto.ExamRootCommentDto
import java.time.LocalDateTime

object ExamRootCommentDtoFixture {

    fun create(
        id: Long = 1L,
        content: String = "댓글입니다.",
        author: AuthorDto = AuthorDto(
            id = 1L,
            name = "example",
            avatarUrl = "https://example.com/avatar.jpg",
        ),
        replyCount: Long = 2L,
        isDeleted: Boolean = false,
    ): ExamRootCommentDto {
        return ExamRootCommentDto(
            id = id,
            content = content,
            author = author,
            replyCount = replyCount,
            isDeleted = isDeleted,
            createdAt = LocalDateTime.MIN,
            updatedAt = LocalDateTime.MIN
        )
    }
}