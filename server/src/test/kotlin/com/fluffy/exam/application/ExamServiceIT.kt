package com.fluffy.exam.application

import com.fluffy.auth.domain.MemberRepository
import com.fluffy.auth.fixture.MemberFixture
import com.fluffy.exam.application.request.CreateExamRequest
import com.fluffy.exam.application.request.UpdateExamQuestionsRequest
import com.fluffy.exam.application.request.UpdateExamTitleRequest
import com.fluffy.exam.application.request.question.LongAnswerQuestionRequest
import com.fluffy.exam.domain.ExamRepository
import com.fluffy.exam.domain.QuestionRepository
import com.fluffy.exam.domain.QuestionType
import com.fluffy.exam.domain.findByIdOrThrow
import com.fluffy.exam.fixture.ExamFixture
import com.fluffy.global.web.Accessor
import com.fluffy.support.AbstractIntegrationTest
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class ExamServiceIT(
    private val examService: ExamService,
    private val memberRepository: MemberRepository,
    private val examRepository: ExamRepository,
    private val questionRepository: QuestionRepository
) : AbstractIntegrationTest() {

    @Test
    fun `시험을 생성할 수 있다`() {
        // given
        val member = memberRepository.save(MemberFixture.create())

        // when
        val request = CreateExamRequest(
            title = "시험 제목",
            accessor = Accessor(member.id)
        )
        val response = examService.create(request)

        // then
        val savedExam = examRepository.findByIdOrThrow(response.id)

        savedExam.title shouldBe request.title
        savedExam.memberId shouldBe member.id
    }

    @Test
    fun `시험 문제들을 수정할 수 있다`() {
        // given
        val member = memberRepository.save(MemberFixture.create())
        val exam = examRepository.save(ExamFixture.create(memberId = member.id))

        // when
        val request = UpdateExamQuestionsRequest(
            examId = exam.id,
            questions = listOf(
                LongAnswerQuestionRequest(
                    text = "새로운 질문",
                    passage = "새로운 지문",
                    type = "LONG_ANSWER",
                )
            ),
            accessor = Accessor(member.id)
        )
        examService.updateQuestions(request)

        // then
        val questions = questionRepository.findAll()
        questions.size shouldBe 1
        questions[0].text shouldBe "새로운 질문"
        questions[0].passage shouldBe "새로운 지문"
        questions[0].type shouldBe QuestionType.LONG_ANSWER
    }

    @Test
    fun `시험 제목을 수정할 수 있다`() {
        // given
        val member = memberRepository.save(MemberFixture.create())
        val exam = examRepository.save(ExamFixture.create(memberId = member.id))

        // when
        val request = UpdateExamTitleRequest(
            examId = exam.id,
            title = "새로운 시험 제목",
            accessor = Accessor(member.id)
        )
        examService.updateTitle(request)

        // then
        val updatedExam = examRepository.findByIdOrThrow(exam.id)

        updatedExam.title shouldBe "새로운 시험 제목"
    }

    @Test
    fun `시험 설명을 수정할 수 있다`() {
        // given
        val member = memberRepository.save(MemberFixture.create())
        val exam = examRepository.save(ExamFixture.create(memberId = member.id))

        // when
        val request = UpdateExamTitleRequest(
            examId = exam.id,
            title = "새로운 시험 설명",
            accessor = Accessor(member.id)
        )
        examService.updateTitle(request)

        // then
        val updatedExam = examRepository.findByIdOrThrow(exam.id)

        updatedExam.title shouldBe "새로운 시험 설명"
    }
}