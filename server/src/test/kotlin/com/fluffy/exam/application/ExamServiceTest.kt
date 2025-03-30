package com.fluffy.exam.application

import com.fluffy.exam.application.request.*
import com.fluffy.exam.application.request.question.QuestionRequest
import com.fluffy.exam.application.request.question.ShortAnswerQuestionRequest
import com.fluffy.exam.application.response.CreateExamResponse
import com.fluffy.exam.domain.Exam
import com.fluffy.exam.domain.ExamRepository
import com.fluffy.exam.domain.findByIdOrThrow
import com.fluffy.exam.fixture.ExamFixture
import com.fluffy.global.exception.ForbiddenException
import com.fluffy.global.web.Accessor
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class ExamServiceTest : BehaviorSpec({

    val examRepository = mockk<ExamRepository>()
    val examService = ExamService(examRepository)

    val questions: List<QuestionRequest> = listOf(
        ShortAnswerQuestionRequest(
            text = "질문",
            passage = "지문",
            type = "SHORT_ANSWER",
            correctAnswer = "정답",
        )
    )

    Given("createExam") {
        val request = CreateExamRequest("시험 제목", Accessor(1L))

        When("시험을 생성할 때") {
            val exam = ExamFixture.create(id = 1L)
            every { examRepository.save(any()) } returns exam

            val actual = examService.create(request)
            val expected = CreateExamResponse(id = exam.id, title = exam.title)

            Then("시험을 생성하고 응답을 반환한다.") {
                actual shouldBe expected
            }
        }
    }

    Given("updateQuestions") {
        val examId = 1L
        val memberId = 1L
        val request = UpdateExamQuestionsRequest(
            accessor = Accessor(memberId),
            examId = examId,
            questions = questions,
        )

        When("사용자와 시험 작성자가 다른 경우") {
            val exam = mockk<Exam>()
            every { examRepository.findByIdOrThrow(any()) } returns exam
            every { exam.isNotWrittenBy(any()) } returns true

            Then("예외를 발생시킨다.") {
                shouldThrow<ForbiddenException> {
                    examService.updateQuestions(request)
                }.message shouldBe "해당 사용자가 작성한 시험이 아닙니다. 사용자 식별자: $memberId, 시험 식별자: $examId"
            }
        }

        When("사용자와 시험 작성자가 같은 경우") {
            val exam = mockk<Exam>()
            every { examRepository.findByIdOrThrow(any()) } returns exam
            every { exam.isNotWrittenBy(any()) } returns false
            every { exam.updateQuestions(any()) } returns Unit

            examService.updateQuestions(request)

            Then("시험의 문제를 업데이트한다.") {
                verify { exam.updateQuestions(any()) }
            }
        }
    }

    Given("publish") {
        val examId = 1L
        val memberId = 1L
        val request = PublishExamRequest(
            accessor = Accessor(memberId),
            examId = examId,
            questions = questions,
        )

        When("사용자와 시험 작성자가 다른 경우") {
            val exam = mockk<Exam>()
            every { examRepository.findByIdOrThrow(any()) } returns exam
            every { exam.isNotWrittenBy(any()) } returns true

            Then("예외를 발생시킨다.") {
                shouldThrow<ForbiddenException> {
                    examService.publish(request)
                }.message shouldBe "해당 사용자가 작성한 시험이 아닙니다. 사용자 식별자: $memberId, 시험 식별자: $examId"
            }
        }

        When("사용자와 시험 작성자가 같은 경우") {
            val exam = mockk<Exam>()
            every { examRepository.findByIdOrThrow(any()) } returns exam
            every { exam.isNotWrittenBy(any()) } returns false
            every { exam.updateQuestions(any()) } returns Unit
            every { exam.publish() } returns Unit

            examService.publish(request)

            Then("시험을 발행한다.") {
                verify { exam.updateQuestions(any()) }
                verify { exam.publish() }
            }
        }
    }

    Given("updateTitle") {
        val examId = 1L
        val memberId = 1L
        val request = UpdateExamTitleRequest(
            accessor = Accessor(memberId),
            examId = examId,
            title = "시험 제목",
        )

        When("사용자와 시험 작성자가 다른 경우") {
            val exam = mockk<Exam>()
            every { examRepository.findByIdOrThrow(any()) } returns exam
            every { exam.isNotWrittenBy(any()) } returns true

            Then("예외를 발생시킨다.") {
                shouldThrow<ForbiddenException> {
                    examService.updateTitle(request)
                }.message shouldBe "해당 사용자가 작성한 시험이 아닙니다. 사용자 식별자: $memberId, 시험 식별자: $examId"
            }
        }

        When("사용자와 시험 작성자가 같은 경우") {
            val exam = mockk<Exam>()
            every { examRepository.findByIdOrThrow(any()) } returns exam
            every { exam.isNotWrittenBy(any()) } returns false
            every { exam.updateTitle(any()) } returns Unit

            examService.updateTitle(request)

            Then("시험의 제목을 업데이트한다.") {
                verify { exam.updateTitle(any()) }
            }
        }
    }

    Given("updateDescription") {
        val examId = 1L
        val memberId = 1L
        val request = UpdateExamDescriptionRequest(
            accessor = Accessor(memberId),
            examId = examId,
            description = "시험 설명",
        )

        When("사용자와 시험 작성자가 다른 경우") {
            val exam = mockk<Exam>()
            every { examRepository.findByIdOrThrow(any()) } returns exam
            every { exam.isNotWrittenBy(any()) } returns true

            Then("예외를 발생시킨다.") {
                shouldThrow<ForbiddenException> {
                    examService.updateDescription(request)
                }.message shouldBe "해당 사용자가 작성한 시험이 아닙니다. 사용자 식별자: $memberId, 시험 식별자: $examId"
            }
        }

        When("사용자와 시험 작성자가 같은 경우") {
            val exam = mockk<Exam>()
            every { examRepository.findByIdOrThrow(any()) } returns exam
            every { exam.isNotWrittenBy(any()) } returns false
            every { exam.updateDescription(any()) } returns Unit

            examService.updateDescription(request)

            Then("시험의 설명을 업데이트한다.") {
                verify { exam.updateDescription(any()) }
            }
        }
    }
})