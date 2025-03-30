package com.fluffy.comment.application

import com.fluffy.comment.domain.ExamComment
import com.fluffy.comment.domain.ExamCommentRepository
import com.fluffy.comment.domain.findByIdOrThrow
import com.fluffy.comment.fixture.ExamReplyCommentDtoFixture
import com.fluffy.comment.fixture.ExamRootCommentDtoFixture
import com.fluffy.exam.domain.Exam
import com.fluffy.exam.domain.ExamRepository
import com.fluffy.exam.domain.findByIdOrThrow
import com.fluffy.global.exception.NotFoundException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.data.repository.findByIdOrNull

class ExamCommentQueryServiceTest : BehaviorSpec({

    val examRepository = mockk<ExamRepository>()
    val examCommentRepository = mockk<ExamCommentRepository>()
    val examCommentQueryService = ExamCommentQueryService(examRepository, examCommentRepository)

    Given("getRootComments") {
        val examId = 1L
        val exam = mockk<Exam>()

        every { exam.id } returns examId

        When("시험이 존재하지 않는 경우") {
            every { examRepository.findByIdOrNull(any()) } returns null

            Then("예외를 발생시킨다.") {
                shouldThrow<NotFoundException> { examCommentQueryService.getRootComments(examId) }
                    .message shouldBe "존재하지 않는 시험입니다. 시험 식별자: $examId"
            }
        }

        When("시험이 존재하는 경우") {
            every { examRepository.findByIdOrThrow(any()) } returns exam

            val rootComments = listOf(
                ExamRootCommentDtoFixture.create(id = 1L),
                ExamRootCommentDtoFixture.create(id = 2L)
            )
            every { examCommentRepository.findRootComments(any()) } returns rootComments
            val actual = examCommentQueryService.getRootComments(examId)

            Then("루트 댓글들의 정보를 반환한다.") {
                actual shouldBe rootComments
            }

            And("삭제된 댓글이 있을 경우") {
                val deletedRootComments = listOf(
                    ExamRootCommentDtoFixture.create(id = 1L, isDeleted = true),
                    ExamRootCommentDtoFixture.create(id = 2L, isDeleted = false)
                )
                every { examCommentRepository.findRootComments(any()) } returns deletedRootComments

                val response  = examCommentQueryService.getRootComments(examId)
                val expected = listOf(
                    ExamRootCommentDtoFixture.create(id = 1L, isDeleted = true).asDeleted(),
                    ExamRootCommentDtoFixture.create(id = 2L, isDeleted = false)
                )

                Then("삭제된 댓글을 마스킹 처리한 루트 댓글 정보를 반환한다.") {
                    response shouldBe expected
                }
            }
        }
    }

    Given("getReplyComments") {
        val parentCommentId = 1L

        When("부모 댓글이 존재하지 않는 경우") {
            Then("예외를 발생시킨다.") {
                every { examCommentRepository.findByIdOrNull(any()) } returns null

                shouldThrow<NotFoundException> { examCommentQueryService.getReplyComments(parentCommentId) }
                    .message shouldBe "존재하지 않는 댓글입니다. 댓글 식별자: $parentCommentId"
            }
        }

        When("부모 댓글이 존재하는 경우") {
            val rootComment = mockk<ExamComment>()

            every { examCommentRepository.findByIdOrThrow(any()) } returns rootComment
            every { rootComment.isDeleted() } returns false

            And("답글이 존재하는 경우") {
                val replies = listOf(
                    ExamReplyCommentDtoFixture.create(id = 1L),
                    ExamReplyCommentDtoFixture.create(id = 2L)
                )
                every { examCommentRepository.findReplyComments(any()) } returns replies

                val actual = examCommentQueryService.getReplyComments(parentCommentId)

                Then("대댓글 리스트를 반환한다.") {
                    actual shouldBe replies
                }
            }

            And("삭제된 댓글이고 대댓글도 없는 경우") {
                every { rootComment.isDeleted() } returns true
                every { examCommentRepository.findReplyComments(any()) } returns emptyList()

                Then("예외를 발생시킨다.") {
                    shouldThrow<NotFoundException> { examCommentQueryService.getReplyComments(parentCommentId) }
                        .message shouldBe "존재하지 않는 댓글입니다. 댓글 식별자: $parentCommentId"
                }
            }
        }
    }
})

