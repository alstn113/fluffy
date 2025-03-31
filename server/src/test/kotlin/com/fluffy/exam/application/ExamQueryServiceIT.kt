package com.fluffy.exam.application

import com.fluffy.auth.domain.MemberRepository
import com.fluffy.auth.fixture.MemberFixture
import com.fluffy.exam.application.response.AnswerQuestionResponse
import com.fluffy.exam.application.response.AnswerQuestionWithAnswersResponse
import com.fluffy.exam.domain.Exam
import com.fluffy.exam.domain.ExamRepository
import com.fluffy.exam.domain.ExamStatus
import com.fluffy.exam.domain.Question
import com.fluffy.global.web.Accessor
import com.fluffy.reaction.domain.Reaction
import com.fluffy.reaction.domain.ReactionRepository
import com.fluffy.reaction.domain.ReactionType
import com.fluffy.submission.domain.Answer
import com.fluffy.submission.domain.Submission
import com.fluffy.submission.domain.SubmissionRepository
import com.fluffy.support.AbstractIntegrationTest
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageRequest
import java.time.temporal.ChronoUnit

class ExamQueryServiceIT(
    private val memberRepository: MemberRepository,
    private val examRepository: ExamRepository,
    private val examQueryService: ExamQueryService,
    private val reactionRepository: ReactionRepository,
    private val submissionRepository: SubmissionRepository,
) : AbstractIntegrationTest() {

    @Test
    fun `출제된 시험을 페이지네이션으로 조회할 수 있다`() {
        // given
        val member = memberRepository.save(MemberFixture.create())

        createExam(memberId = member.id, isPublished = true, questionCount = 5, likeCount = 3)
        createExam(memberId = member.id, isPublished = false, questionCount = 3)
        val exam3 = createExam(memberId = member.id, isPublished = true, questionCount = 3)
        createExam(memberId = member.id, isPublished = false, questionCount = 2)
        val exam5 = createExam(memberId = member.id, isPublished = true, questionCount = 2, likeCount = 3)

        // when
        val pageRequest = PageRequest.of(0, 2)
        val result = examQueryService.getPublishedExamSummaries(pageable = pageRequest)

        // then
        result.content.size shouldBe 2

        result.content[0].id shouldBe exam5.id
        result.content[0].likeCount shouldBe 3
        result.content[0].questionCount shouldBe 2

        result.content[1].id shouldBe exam3.id
        result.content[1].likeCount shouldBe 0
        result.content[1].questionCount shouldBe 3

        result.pageInfo.currentPage shouldBe 0
        result.pageInfo.totalPages shouldBe 2
        result.pageInfo.totalElements shouldBe 3
        result.pageInfo.hasNext shouldBe true
        result.pageInfo.hasPrevious shouldBe false
    }

    @Test
    fun `사용자가 출제한 시험을 페이지네이션으로 조회할 수 있다`() {
        // given
        val member1 = memberRepository.save(MemberFixture.create())
        val member2 = memberRepository.save(MemberFixture.create())

        val exam1 = createExam(memberId = member1.id, isPublished = true, questionCount = 5, likeCount = 3)
        createExam(memberId = member1.id, isPublished = false, questionCount = 3)
        createExam(memberId = member2.id, isPublished = true, questionCount = 3)
        val exam4 = createExam(memberId = member1.id, isPublished = true, questionCount = 2)
        createExam(memberId = member2.id, isPublished = false, questionCount = 2, likeCount = 3)

        // when
        val pageRequest = PageRequest.of(0, 2)
        val response = examQueryService.getMyExamSummaries(
            pageable = pageRequest,
            status = ExamStatus.PUBLISHED,
            accessor = Accessor(member1.id)
        )

        // then
        response.content.size shouldBe 2

        response.content[0].id shouldBe exam4.id
        response.content[0].questionCount shouldBe 2

        response.content[1].id shouldBe exam1.id
        response.content[1].questionCount shouldBe 5

        response.pageInfo.currentPage shouldBe 0
        response.pageInfo.totalPages shouldBe 1
        response.pageInfo.totalElements shouldBe 2
        response.pageInfo.hasNext shouldBe false
        response.pageInfo.hasPrevious shouldBe false
    }

    @Test
    fun `시험에 대한 상세 요약 정보를 조회할 수 있다`() {
        // given
        val member = memberRepository.save(MemberFixture.create())
        val exam = createExam(memberId = member.id, isPublished = true, questionCount = 5, likeCount = 3)
        reactionRepository.save(
            Reaction.create(
                targetType = "EXAM",
                targetId = exam.id,
                memberId = member.id,
                type = ReactionType.LIKE
            )
        )

        // when
        val response = examQueryService.getExamDetailSummary(
            examId = exam.id,
            accessor = Accessor(member.id)
        )

        // then
        response.id shouldBe exam.id
        response.questionCount shouldBe 5
        response.likeCount shouldBe 4
        response.isLiked shouldBe true
    }

    @Test
    fun `시험에 대한 상세 정보를 조회할 수 있다`() {
        // given
        val member = memberRepository.save(MemberFixture.create())
        val exam = createExam(memberId = member.id, isPublished = true, questionCount = 2, likeCount = 3)

        // when
        val response = examQueryService.getExamDetail(examId = exam.id)

        // then
        response.id shouldBe exam.id
        response.questions shouldContainExactly listOf(
            AnswerQuestionResponse(
                id = 1L,
                text = "질문",
                passage = "지문",
                type = "SHORT_ANSWER"
            ),
            AnswerQuestionResponse(
                id = 2L,
                text = "질문",
                passage = "지문",
                type = "SHORT_ANSWER"
            ),
        )
    }

    @Test
    fun `시험에 대한 상세 정보와 정답을 조회할 수 있다`() {
        // given
        val member = memberRepository.save(MemberFixture.create())
        val questionCount = 2
        val exam = createExam(
            memberId = member.id,
            isPublished = true,
            questionCount = questionCount,
            likeCount = 3
        )
        createSubmission(
            examId = exam.id,
            memberId = member.id,
            answerCount = questionCount
        )

        // when
        val response = examQueryService.getExamWithAnswers(
            examId = exam.id,
            accessor = Accessor(member.id)
        )

        // then
        response.id shouldBe exam.id
        response.questions shouldContainExactly listOf(
            AnswerQuestionWithAnswersResponse(
                id = 1L,
                text = "질문",
                passage = "지문",
                type = "SHORT_ANSWER",
                correctAnswer = "정답"
            ),
            AnswerQuestionWithAnswersResponse(
                id = 2L,
                text = "질문",
                passage = "지문",
                type = "SHORT_ANSWER",
                correctAnswer = "정답"
            ),
        )

    }

    @Test
    fun `제출한 시험들에 대한 요약 정보를 페이지네이션으로 조회할 수 있다`() {
        val member = memberRepository.save(MemberFixture.create())

        val exam1 = createExam(memberId = member.id, isPublished = true, questionCount = 1, likeCount = 3)
        val exam2 = createExam(memberId = member.id, isPublished = true, questionCount = 2)

        submissionRepository.save(
            createSubmission(
                examId = exam1.id,
                memberId = member.id,
                answerCount = 1
            )
        )
        val submission2 = submissionRepository.save(
            createSubmission(
                examId = exam2.id,
                memberId = member.id,
                answerCount = 2
            )
        )
        val submission3 = submissionRepository.save(
            createSubmission(
                examId = exam1.id,
                memberId = member.id,
                answerCount = 1
            )
        )

        // when
        val pageRequest = PageRequest.of(0, 2)
        val response = examQueryService.getSubmittedExamSummaries(
            pageable = pageRequest,
            accessor = Accessor(member.id)
        )

        // then
        response.content.size shouldBe 2

        response.content[0].examId shouldBe exam1.id
        response.content[0].submissionCount shouldBe 2
        println("확인 response.content[0].lastSubmissionDate 6자리 = ${response.content[0].lastSubmissionDate}")
        println("truncate = " + response.content[0].lastSubmissionDate.truncatedTo(ChronoUnit.MICROS))
        println("확인 submission3.createdAt 9자리 = ${submission3.createdAt}")
        println("truncate = " + submission3.createdAt.truncatedTo(ChronoUnit.MICROS))
        response.content[0].lastSubmissionDate shouldBe submission3.createdAt.truncatedTo(ChronoUnit.MICROS)

        response.content[1].examId shouldBe exam2.id
        response.content[1].submissionCount shouldBe 1
        response.content[1].lastSubmissionDate shouldBe submission2.createdAt.truncatedTo(ChronoUnit.MICROS)

        response.pageInfo.currentPage shouldBe 0
        response.pageInfo.totalPages shouldBe 1
        response.pageInfo.totalElements shouldBe 2
        response.pageInfo.hasNext shouldBe false
        response.pageInfo.hasPrevious shouldBe false
    }

    private fun createExam(
        memberId: Long,
        isPublished: Boolean = true,
        questionCount: Int = 3,
        likeCount: Int = 0,
    ): Exam {
        val exam = Exam.create(
            title = "시험 제목",
            memberId = memberId,
        )
        exam.updateQuestions(
            List(questionCount) {
                Question.shortAnswer(
                    text = "질문",
                    passage = "지문",
                    correctAnswer = "정답",
                )
            }
        )
        if (isPublished) {
            exam.publish()
        }

        val savedExam = examRepository.save(exam)

        repeat(likeCount) {
            val member = memberRepository.save(MemberFixture.create())
            reactionRepository.save(
                Reaction.create(
                    targetType = "EXAM",
                    targetId = exam.id,
                    memberId = member.id,
                    type = ReactionType.LIKE
                )
            )
        }

        return savedExam
    }

    private fun createSubmission(examId: Long, memberId: Long, answerCount: Int): Submission {
        val answers = List(answerCount) {
            Answer.textAnswer(
                questionId = it.toLong(),
                text = "답안",
            )
        }

        val submission = Submission.create(
            examId = examId,
            memberId = memberId,
            answers = answers,
        )

        return submissionRepository.save(submission)
    }
}