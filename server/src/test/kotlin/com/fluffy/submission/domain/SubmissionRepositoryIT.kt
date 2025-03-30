package com.fluffy.submission.domain

import com.fluffy.auth.domain.MemberRepository
import com.fluffy.auth.fixture.MemberFixture
import com.fluffy.exam.domain.Exam
import com.fluffy.exam.domain.ExamRepository
import com.fluffy.exam.domain.Question
import com.fluffy.exam.fixture.ExamFixture
import com.fluffy.global.exception.NotFoundException
import com.fluffy.support.AbstractIntegrationTest
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class SubmissionRepositoryIT(
    private val submissionRepository: SubmissionRepository,
    private val examRepository: ExamRepository,
    private val memberRepository: MemberRepository
) : AbstractIntegrationTest() {

    @Test
    fun `제출 식별자로 제출을 조회할 수 있다`() {
        // given
        val member = memberRepository.save(MemberFixture.create())
        val exam = createExam(memberId = member.id)
        val submission = createSubmission(examId = exam.id, memberId = member.id)

        // when
        val foundSubmission = submissionRepository.findByIdOrThrow(submission.id)

        // then
        foundSubmission.id shouldBe submission.id
    }

    @Test
    fun `존재하지 않는 제출 식별자로 제출을 조회할 경우 예외를 발생시킨다`() {
        // given
        val nonExistentSubmissionId = -1L

        // when & then
        shouldThrow<NotFoundException> {
            submissionRepository.findByIdOrThrow(nonExistentSubmissionId)
        }.message shouldBe "존재하지 않는 제출입니다. 제출 식별자: $nonExistentSubmissionId"
    }

    @Test
    fun `시험에 대한 제출 요약 목록을 조회할 수 있다`() {
        // given
        val member1 = memberRepository.save(MemberFixture.create())
        val member2 = memberRepository.save(MemberFixture.create())

        val exam1 = createExam(memberId = member1.id)
        val exam2 = createExam(memberId = member2.id)

        val submission1 = createSubmission(examId = exam1.id, memberId = member1.id)
        val submission2 = createSubmission(examId = exam1.id, memberId = member2.id)
        val submission3 = createSubmission(examId = exam2.id, memberId = member1.id)
        val submission4 = createSubmission(examId = exam1.id, memberId = member2.id)
        val submission5 = createSubmission(examId = exam2.id, memberId = member2.id)

        // when
        val summaries = submissionRepository.findSubmissionSummariesByExamId(exam1.id)

        // then
        summaries.size shouldBe 3
        summaries.map { it.participant.id } shouldContainExactly listOf(member2.id, member2.id, member1.id)
        summaries.map { it.id }.size shouldBe 3
        summaries.map { it.submittedAt } shouldContainExactly
                listOf(submission4.createdAt, submission2.createdAt, submission1.createdAt)
    }

    @Test
    fun `내 제출 요약 목록을 조회할 수 있다`() {
        // given
        val member1 = memberRepository.save(MemberFixture.create())
        val member2 = memberRepository.save(MemberFixture.create())

        val exam1 = createExam(memberId = member1.id)
        val exam2 = createExam(memberId = member2.id)

        val submission1 = createSubmission(examId = exam1.id, memberId = member1.id)
        val submission2 = createSubmission(examId = exam1.id, memberId = member2.id)
        val submission3 = createSubmission(examId = exam2.id, memberId = member1.id)
        val submission4 = createSubmission(examId = exam1.id, memberId = member1.id)
        val submission5 = createSubmission(examId = exam2.id, memberId = member2.id)

        // when
        val summaries = submissionRepository.findMySubmissionSummaries(exam1.id, member1.id)

        // then
        summaries.size shouldBe 2
        summaries.map { it.submissionId } shouldContainExactly listOf(submission4.id, submission1.id)
        summaries.map { it.submittedAt } shouldContainExactly
                listOf(submission4.createdAt, submission1.createdAt)
    }

    private fun createExam(memberId: Long): Exam {
        val exam = examRepository.save(ExamFixture.create(memberId = memberId))
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

    private fun createSubmission(examId: Long, memberId: Long): Submission {
        val submission = Submission.create(
            examId = examId,
            memberId = memberId,
            answers = listOf(
                Answer.textAnswer(
                    questionId = examId, // 각각 질문 하나씩 있다고 가정
                    text = "답안",
                )
            )
        )
        return submissionRepository.save(submission)
    }
}