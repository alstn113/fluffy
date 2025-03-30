package com.fluffy.submission.application

import com.fluffy.auth.domain.MemberRepository
import com.fluffy.auth.fixture.MemberFixture
import com.fluffy.exam.domain.Exam
import com.fluffy.exam.domain.ExamRepository
import com.fluffy.exam.domain.Question
import com.fluffy.global.web.Accessor
import com.fluffy.submission.application.response.SubmissionDetailResponse
import com.fluffy.submission.application.response.TextAnswerResponse
import com.fluffy.submission.domain.Answer
import com.fluffy.submission.domain.Submission
import com.fluffy.submission.domain.SubmissionRepository
import com.fluffy.submission.domain.dto.MySubmissionSummaryDto
import com.fluffy.submission.domain.dto.ParticipantDto
import com.fluffy.submission.domain.dto.SubmissionSummaryDto
import com.fluffy.support.AbstractIntegrationTest
import io.kotest.matchers.collections.shouldContainExactly
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
        response shouldContainExactly listOf(
            SubmissionSummaryDto(
                id = submission2.id,
                participant = ParticipantDto(
                    id = member2.id,
                    email = member2.email,
                    name = member2.name,
                    avatarUrl = member2.avatarUrl,
                ),
                submittedAt = submission2.createdAt
            ),
            SubmissionSummaryDto(
                id = submission1.id,
                participant = ParticipantDto(
                    id = member1.id,
                    email = member1.email,
                    name = member1.name,
                    avatarUrl = member1.avatarUrl,
                ),
                submittedAt = submission1.createdAt
            )
        )
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
        response shouldBe SubmissionDetailResponse(
            id = submission.id,
            answers = listOf(
                TextAnswerResponse(
                    id = submission.id,
                    questionId = 1L,
                    text = "질문",
                    type = "SHORT_ANSWER",
                    answer = "답안",
                    correctAnswer = "정답",
                )
            ),
            participant = ParticipantDto(
                id = member.id,
                name = member.name,
                email = member.email,
                avatarUrl = member.avatarUrl,
            ),
            submittedAt = submission.createdAt,
        )
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
        response shouldContainExactly listOf(
            MySubmissionSummaryDto(
                submissionId = submission4.id,
                submittedAt = submission4.createdAt,
            ),
            MySubmissionSummaryDto(
                submissionId = submission1.id,
                submittedAt = submission1.createdAt,
            ),
        )
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