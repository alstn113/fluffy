package com.fluffy.comment.application

import com.fluffy.auth.domain.MemberRepository
import com.fluffy.auth.fixture.MemberFixture
import com.fluffy.comment.application.request.CreateExamCommentRequest
import com.fluffy.comment.application.request.DeleteExamCommentRequest
import com.fluffy.comment.domain.ExamCommentRepository
import com.fluffy.comment.domain.findByIdOrThrow
import com.fluffy.comment.fixture.ExamCommentFixture
import com.fluffy.exam.domain.Exam
import com.fluffy.exam.domain.ExamRepository
import com.fluffy.exam.domain.Question
import com.fluffy.exam.fixture.ExamFixture
import com.fluffy.support.AbstractIntegrationTest
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class ExamCommentServiceIT(
    private val examCommentService: ExamCommentService,
    private val memberRepository: MemberRepository,
    private val examRepository: ExamRepository,
    private val examCommentRepository: ExamCommentRepository,
) : AbstractIntegrationTest() {

    @Test
    fun `댓글을 작성할 수 있다`() {
        // given
        val member = memberRepository.save(MemberFixture.create())
        val exam = createExam(member.id)

        // when
        val request = CreateExamCommentRequest(
            content = "댓글 내용",
            examId = exam.id,
            memberId = member.id,
            parentCommentId = null
        )
        val response = examCommentService.createComment(request)

        // then
        val savedComment = examCommentRepository.findByIdOrThrow(response.id)

        savedComment.id shouldBe response.id
        savedComment.content shouldBe request.content
        savedComment.memberId shouldBe member.id
        savedComment.parentCommentId shouldBe null
    }

    @Test
    fun `답글을 작성할 수 있다`() {
        // given
        val member = memberRepository.save(MemberFixture.create())
        val exam = createExam(member.id)

        val parentComment = examCommentRepository.save(
            ExamCommentFixture.create(
                memberId = member.id,
                examId = exam.id,
                content = "부모 댓글"
            )
        )

        // when
        val request = CreateExamCommentRequest(
            content = "답글 내용",
            examId = exam.id,
            memberId = member.id,
            parentCommentId = parentComment.id
        )
        val response = examCommentService.createComment(request)

        // then
        val savedComment = examCommentRepository.findByIdOrThrow(response.id)

        savedComment.id shouldBe response.id
        savedComment.content shouldBe request.content
        savedComment.memberId shouldBe member.id
        savedComment.parentCommentId shouldBe parentComment.id
    }

    @Test
    fun `댓글을 삭제할 수 있다`() {
        // given
        val member = memberRepository.save(MemberFixture.create())
        val exam = createExam(member.id)

        val comment = examCommentRepository.save(
            ExamCommentFixture.create(
                memberId = member.id,
                examId = exam.id,
                content = "댓글"
            )
        )

        // when
        val request = DeleteExamCommentRequest(
            commentId = comment.id,
            memberId = member.id
        )
        val response = examCommentService.deleteComment(request)

        // then
        val deletedComment = examCommentRepository.findByIdOrThrow(response)
        deletedComment.isDeleted() shouldBe true
    }

    private fun createExam(memberId: Long): Exam {
        val exam = ExamFixture.create(memberId = memberId)
        exam.updateQuestions(
            listOf(
                Question.shortAnswer(
                    text = "질문",
                    passage = "지문",
                    correctAnswer = "정답"
                )
            )
        )
        exam.publish()

        return examRepository.save(exam)
    }
}