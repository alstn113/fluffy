package com.fluffy.submission.application

import com.fluffy.auth.domain.MemberRepository
import com.fluffy.auth.fixture.MemberFixture
import com.fluffy.exam.domain.Exam
import com.fluffy.exam.domain.ExamRepository
import com.fluffy.exam.domain.Question
import com.fluffy.global.web.Accessor
import com.fluffy.submission.domain.Answer
import com.fluffy.submission.domain.Submission
import com.fluffy.submission.domain.SubmissionRepository
import com.fluffy.support.AbstractIntegrationTest
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class SubmissionQueryServiceIT(
    private val submissionQueryService: SubmissionQueryService,
    private val memberRepository: MemberRepository,
    private val examRepository: ExamRepository,
    private val submissionRepository: SubmissionRepository
) : AbstractIntegrationTest() {

    @Test
    fun `시험에 대한 제출 요약 목록을 조회할 수 있다`() {
        // given
        val member1 = memberRepository.save(MemberFixture.create())
        val member2 = memberRepository.save(MemberFixture.create())

        val exam = createExam(memberId = member1.id)

        val submission1 = createSubmission(examId = exam.id, memberId = member1.id)
        val submission2 = createSubmission(examId = exam.id, memberId = member2.id)

        // when
        val response = submissionQueryService.getSummariesByExamId(
            examId = exam.id,
            accessor = Accessor(member1.id),
        )

        // then
        response[0].id shouldBe submission2.id
        response[0].participant.id shouldBe member2.id
//        response[0].submittedAt shouldBe submission1.createdAt

        response[1].id shouldBe submission1.id
        response[1].participant.id shouldBe member1.id
//        response[1].submittedAt shouldBe submission2.createdAt
    }

    @Test
    fun `시험 제출 상세 정보를 조회할 수 있다`() {
        // given
        val member = memberRepository.save(MemberFixture.create())
        val exam = createExam(memberId = member.id)
        val submission = createSubmission(examId = exam.id, memberId = member.id)

        // when
        val response = submissionQueryService.getDetail(
            examId = exam.id,
            submissionId = submission.id,
            accessor = Accessor(member.id),
        )

        // then
        response.id shouldBe submission.id
        response.answers.size shouldBe 1
        response.answers[0].id shouldBe submission.id
        response.participant.id shouldBe member.id
//        response.submittedAt shouldBe submission.createdAt
    }

    @Test
    fun `내 제출 요약 목록을 조회할 수 있다`() {
        // given
        val member1 = memberRepository.save(MemberFixture.create())
        val member2 = memberRepository.save(MemberFixture.create())

        val exam1 = createExam(memberId = member1.id)
        val exam2 = createExam(memberId = member2.id)

        val submission1 = createSubmission(examId = exam1.id, memberId = member1.id)
        createSubmission(examId = exam1.id, memberId = member2.id)
        createSubmission(examId = exam2.id, memberId = member1.id)
        val submission4 = createSubmission(examId = exam1.id, memberId = member1.id)

        // when
        val response = submissionQueryService.getMySubmissionSummaries(
            examId = exam1.id,
            accessor = Accessor(member1.id),
        )

        // then
        response[0].submissionId shouldBe submission4.id
//        response[0].submittedAt shouldBe submission4.createdAt

        response[1].submissionId shouldBe submission1.id
//        response[1].submittedAt shouldBe submission1.createdAt
    }

    private fun createExam(memberId: Long): Exam {
        val exam = Exam.create(
            title = "시험 제목",
            memberId = memberId,
        )
        exam.updateQuestions(
            listOf(
                Question.shortAnswer(
                    text = "질문",
                    passage = "지문",
                    correctAnswer = "정답",
                )
            )
        )
        exam.publish()

        return examRepository.save(exam)
    }

    private fun createSubmission(examId: Long, memberId: Long): Submission {
        return submissionRepository.save(
            Submission.create(
                examId = examId,
                memberId = memberId,
                answers = listOf(
                    Answer.textAnswer("답안", 1L)
                )
            )
        )
    }
}