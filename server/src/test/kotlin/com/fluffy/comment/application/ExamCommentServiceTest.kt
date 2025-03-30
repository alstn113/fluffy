package com.fluffy.comment.application

import com.fluffy.auth.domain.MemberRepository
import com.fluffy.auth.domain.findByIdOrThrow
import com.fluffy.auth.fixture.MemberFixture
import com.fluffy.comment.application.request.CreateExamCommentRequest
import com.fluffy.comment.application.request.DeleteExamCommentRequest
import com.fluffy.comment.application.response.CreateExamCommentResponse
import com.fluffy.comment.domain.ExamCommentRepository
import com.fluffy.comment.domain.findByIdOrThrow
import com.fluffy.comment.fixture.ExamCommentFixture
import com.fluffy.exam.domain.Exam
import com.fluffy.exam.domain.ExamRepository
import com.fluffy.exam.domain.findByIdOrThrow
import com.fluffy.global.exception.BadRequestException
import com.fluffy.global.exception.ForbiddenException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class ExamCommentServiceTest : BehaviorSpec({

    val memberRepository = mockk<MemberRepository>()
    val examRepository = mockk<ExamRepository>()
    val examCommentRepository = mockk<ExamCommentRepository>()
    val examCommentService = ExamCommentService(memberRepository, examRepository, examCommentRepository)

    Given("createComment") {
        val request = mockk<CreateExamCommentRequest>()
        val exam = mockk<Exam>()
        val memberId = 1L
        val examId = 1L
        val member = MemberFixture.create(id = memberId)

        every { request.content } returns "댓글입니다."
        every { request.memberId } returns memberId
        every { request.examId } returns examId
        every { memberRepository.findByIdOrThrow(any()) } returns member
        every { examRepository.findByIdOrThrow(any()) } returns exam

        When("시험이 출제되지 않은 경우") {
            every { exam.isNotPublished() } returns true

            Then("예외를 발생시킨다.") {
                shouldThrow<BadRequestException> { examCommentService.createComment(request) }
                    .message shouldBe "출제되지 않은 시험에는 댓글을 작성할 수 없습니다."
            }
        }

        When("시험이 출제된 경우") {
            every { exam.isNotPublished() } returns false

            And("부모 댓글 식별자가 없는 경우") {
                every { request.parentCommentId } returns null

                val rootComment = ExamCommentFixture.create(
                    id = 1L,
                    content = request.content,
                    memberId = memberId,
                    examId = examId
                )

                every { examCommentRepository.save(any()) } returns rootComment

                val actual = examCommentService.createComment(request)
                val expected = CreateExamCommentResponse.of(rootComment, member)

                Then("루트 댓글을 생성하고, 댓글 정보를 반환한다.") {
                    actual shouldBe expected
                }
            }

            And("부모 댓글 식별자가 있는 경우") {
                val parentCommentId = 2L

                val parentComment = ExamCommentFixture.create(id = parentCommentId, examId = examId)
                val replyComment = ExamCommentFixture.create(
                    id = 3L,
                    content = request.content,
                    memberId = memberId,
                    parentCommentId = parentCommentId
                )

                every { request.parentCommentId } returns parentCommentId
                every { examCommentRepository.findByIdOrThrow(any()) } returns parentComment
                every { examCommentRepository.save(any()) } returns replyComment

                val actual = examCommentService.createComment(request)
                val expected = CreateExamCommentResponse.of(replyComment, member)

                Then("댓글에 대한 답글을 생성하고, 댓글 정보를 반환한다.") {
                    actual shouldBe expected
                }
            }
        }
    }

    Given("deleteComment") {

        When("댓글 작성자가 댓글을 삭제하는 경우") {
            val memberId = 1L
            val commentId = 1L
            val examComment = ExamCommentFixture.create(id = commentId, memberId = memberId)
            val request = DeleteExamCommentRequest(commentId, memberId)

            every { examCommentRepository.findByIdOrThrow(any()) } returns examComment

            val actual = examCommentService.deleteComment(request)

            Then("댓글을 삭제 처리하고, 댓글 식별자를 반환한다.") {
                actual shouldBe commentId
            }
        }

        When("댓글 작성자가 아닌 사용자가 댓글을 삭제하는 경우") {
            val memberId = 1L
            val commentId = 1L
            val examComment = ExamCommentFixture.create(id = commentId, memberId = 2L)
            val request = DeleteExamCommentRequest(commentId, memberId)

            every { examCommentRepository.findByIdOrThrow(any()) } returns examComment

            Then("예외를 발생시킨다.") {
                shouldThrow<ForbiddenException> { examCommentService.deleteComment(request) }
                    .message shouldBe "댓글 작성자만 삭제할 수 있습니다."
            }
        }
    }
})
