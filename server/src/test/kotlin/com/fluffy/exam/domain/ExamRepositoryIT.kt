package com.fluffy.exam.domain

import com.fluffy.auth.domain.MemberRepository
import com.fluffy.auth.fixture.MemberFixture
import com.fluffy.exam.domain.dto.ExamSummaryDto
import com.fluffy.exam.domain.dto.MyExamSummaryDto
import com.fluffy.exam.domain.dto.SubmittedExamSummaryDto
import com.fluffy.exam.fixture.ExamFixture
import com.fluffy.global.exception.NotFoundException
import com.fluffy.submission.domain.Answer
import com.fluffy.submission.domain.Submission
import com.fluffy.submission.domain.SubmissionRepository
import com.fluffy.support.AbstractIntegrationTest
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageRequest


class ExamRepositoryIT(
    private val examRepository: ExamRepository,
    private val memberRepository: MemberRepository,
    private val submissionRepository: SubmissionRepository
) : AbstractIntegrationTest() {

    @Test
    fun `시험 식별자로 시험을 조회할 수 있다`() {
        // given
        val member = memberRepository.save(MemberFixture.create())
        val exam = ExamFixture.create(memberId = member.id)
        val savedExam = examRepository.save(exam)

        // when
        val foundExam = examRepository.findByIdOrThrow(savedExam.id)

        // then
        foundExam.id shouldBe savedExam.id
    }

    @Test
    fun `존재하지 않는 시험 식별자로 시험을 조회할 경우 예외를 발생시킨다`() {
        // given
        val nonExistentExamId = -1L

        // when & then
        shouldThrow<NotFoundException> {
            examRepository.findByIdOrThrow(nonExistentExamId)
        }.message shouldBe "존재하지 않는 시험입니다. 시험 식별자: $nonExistentExamId"
    }

    @Test
    fun `출제된 시험 요약 목록을 조회할 수 있다`() {
        // given
        val member1 = memberRepository.save(MemberFixture.create())
        val member2 = memberRepository.save(MemberFixture.create())

        createExam(member1.id, questionCount = 2)
        createExam(member1.id, published = false)
        val publishedExam2 = createExam(member2.id, questionCount = 3)
        createExam(member2.id, published = false)
        val publishedExam3 = createExam(member2.id, questionCount = 5)

        // when
        val pageable = PageRequest.of(0, 2)
        val summaries = examRepository.findPublishedExamSummaries(pageable = pageable)

        // then
        summaries.totalElements shouldBe 3
        summaries.totalPages shouldBe 2
        summaries.number shouldBe 0
        summaries.size shouldBe 2
        summaries.content.map(ExamSummaryDto::id) shouldBe listOf(publishedExam3.id, publishedExam2.id)
        summaries.content.map(ExamSummaryDto::questionCount) shouldBe listOf(5L, 3L)
    }

    @Test
    fun `내가 출제한 시험 요약 목록을 조회할 수 있다`() {
        // given
        val me = memberRepository.save(MemberFixture.create())
        val another = memberRepository.save(MemberFixture.create())

        val publishedExam1 = createExam(me.id, questionCount = 4)
        createExam(me.id, published = false)
        createExam(another.id, questionCount = 3)
        createExam(me.id, published = false)
        createExam(another.id, questionCount = 5)
        val publishedExam4 = createExam(me.id, questionCount = 2)

        // when
        val pageable = PageRequest.of(0, 2)
        val summaries = examRepository.findMyExamSummaries(
            pageable = pageable,
            status = ExamStatus.PUBLISHED,
            memberId = me.id
        )

        // then
        summaries.totalElements shouldBe 2
        summaries.totalPages shouldBe 1
        summaries.number shouldBe 0
        summaries.size shouldBe 2
        summaries.content.map(MyExamSummaryDto::id) shouldBe listOf(publishedExam4.id, publishedExam1.id)
        summaries.content.map(MyExamSummaryDto::questionCount) shouldBe listOf(2L, 4L)
    }

    @Test
    fun `내가 제출한 시험 요약 목록을 조회할 수 있다`() {
        // given
        val member1 = memberRepository.save(MemberFixture.create())
        val member2 = memberRepository.save(MemberFixture.create())

        val publishedExam1 = createExam(member1.id)
        val publishedExam2 = createExam(member1.id)

        createSubmission(
            examId = publishedExam1.id,
            memberId = member1.id,
            answers = listOf(Answer.textAnswer("답1", 1L))
        )
        createSubmission(
            examId = publishedExam2.id,
            memberId = member2.id,
            answers = listOf(Answer.textAnswer("답2", 2L))
        )
        val exam1Submission2 = createSubmission(
            examId = publishedExam1.id,
            memberId = member1.id,
            answers = listOf(Answer.textAnswer("답3", 1L))
        )
        val exam2Submission2 = createSubmission(
            examId = publishedExam2.id,
            memberId = member1.id,
            answers = listOf(Answer.textAnswer("답4", 1L))
        )

        // when
        val pageable = PageRequest.of(0, 2)
        val summaries = examRepository.findSubmittedExamSummaries(pageable = pageable, memberId = member1.id)

        // then
        summaries.totalElements shouldBe 2
        summaries.totalPages shouldBe 1
        summaries.number shouldBe 0
        summaries.size shouldBe 2
        summaries.content.map(SubmittedExamSummaryDto::examId) shouldBe listOf(publishedExam2.id, publishedExam1.id)
        summaries.content.map(SubmittedExamSummaryDto::submissionCount) shouldBe listOf(1L, 2L)
        summaries.content.map(SubmittedExamSummaryDto::lastSubmissionDate) shouldBe
                listOf(exam2Submission2.createdAt, exam1Submission2.createdAt)
    }

    private fun createExam(memberId: Long, published: Boolean = true, questionCount: Int = 1): Exam {
        val exam = ExamFixture.create(memberId = memberId)
        exam.updateQuestions(
            List(questionCount) {
                Question.shortAnswer(
                    text = "질문 $it",
                    passage = "지문 $it",
                    correctAnswer = "정답 $it"
                )
            }
        )

        if (published) {
            exam.publish()
        }

        return examRepository.save(exam)
    }

    private fun createSubmission(
        examId: Long,
        memberId: Long,
        answers: List<Answer>
    ): Submission {
        val submission = Submission.create(
            examId = examId,
            memberId = memberId,
            answers = answers
        )
        return submissionRepository.save(submission)
    }
}