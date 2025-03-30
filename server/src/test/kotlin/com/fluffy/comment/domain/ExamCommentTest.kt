package com.fluffy.comment.domain

import com.fluffy.global.exception.BadRequestException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class ExamCommentTest : StringSpec({

    "시험 댓글은 정상적으로 생성된다." {
        // given
        val content = "댓글"
        val examId = 1L
        val memberId = 1L

        // when
        val comment = ExamComment.create(content, examId, memberId)

        // then
        comment.content shouldBe content
        comment.examId shouldBe examId
        comment.memberId shouldBe memberId
        comment.parentCommentId shouldBe null
        comment.deletedAt shouldBe null
    }

    "댓글을 삭제하면 삭제 상태가 된다." {
        // given
        val comment = ExamComment.create("삭제할 댓글", 1L, 1L)

        // when
        comment.delete()

        // then
        comment.deletedAt shouldNotBe null
    }

    "삭제된 댓글을 삭제할 수 없다." {
        // given
        val comment = ExamComment.create("삭제된 댓글", 1L, 1L)
        comment.delete()

        // when & then
        shouldThrow<BadRequestException> {
            comment.delete()
        }.message shouldBe "이미 삭제된 댓글입니다."
    }

    "삭제된 댓글에 답글을 작성할 수 없다." {
        // given
        val comment = ExamComment.create("삭제된 댓글", 1L, 1L)
        comment.delete()

        // When & Then
        shouldThrow<BadRequestException> {
            comment.reply("삭제된 댓글의 답글", 101L)
        }.message shouldBe "삭제된 댓글에는 답글을 작성할 수 없습니다."
    }

    "댓글에는 답글을 작성할 수 있다." {
        // given
        val rootComment = ExamComment.create("댓글", 1L, 1L)

        // when
        val replyComment = rootComment.reply("답글", 1L)

        // then
        replyComment.content shouldBe "답글"
        replyComment.parentCommentId shouldBe rootComment.id
        replyComment.deletedAt shouldBe null
    }

    "답글에 대한 답글을 작성할 수 없다." {
        // given
        val rootComment = ExamComment.create("부모 댓글", 1L, 1L)
        val replyComment = rootComment.reply("첫 번째 답글", 1L)

        // when & then
        shouldThrow<BadRequestException> {
            replyComment.reply("답글에 대한 답글", 1L)
        }.message shouldBe "답글에는 답글을 작성할 수 없습니다."
    }
})
