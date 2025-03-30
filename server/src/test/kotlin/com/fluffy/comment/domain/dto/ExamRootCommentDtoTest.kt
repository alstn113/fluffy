package com.fluffy.comment.domain.dto

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class ExamRootCommentDtoTest : StringSpec({
    "삭제된 시험 댓글의 정보를 가릴 수 있다." {
        // given
        val examCommentId = 1L
        val replyCount = 3L
        val authorDto = AuthorDto(
            id = 1,
            name = "John",
            avatarUrl = "https://example.com/avatar.png"
        )
        val dto = ExamRootCommentDto(
            id = examCommentId,
            author = authorDto,
            content = "댓글 내용",
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            replyCount = replyCount,
            isDeleted = false,
        )

        // when
        val deletedDto = dto.asDeleted()

        // then
        deletedDto.id shouldBe examCommentId // 유지
        deletedDto.replyCount shouldBe replyCount // 유지

        deletedDto.content shouldBe ""
        deletedDto.author shouldBe authorDto.asDeleted()
        deletedDto.isDeleted shouldBe true
        deletedDto.createdAt shouldBe LocalDateTime.MIN
        deletedDto.updatedAt shouldBe LocalDateTime.MIN
    }
})
