package com.fluffy.submission.application

import com.fluffy.exam.domain.Question
import com.fluffy.exam.domain.QuestionOption
import com.fluffy.exam.domain.QuestionType
import com.fluffy.global.exception.BadRequestException
import com.fluffy.submission.application.request.QuestionResponseRequest
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class AnswerAssemblerTest : BehaviorSpec({

    Given("toAnswer") {
        val question = mockk<Question>()
        val request = mockk<QuestionResponseRequest>()

        When("응답이 비어있는 경우") {
            every { request.answers.isEmpty() } returns true

            Then("예외를 반환한다.") {
                shouldThrow<BadRequestException> {
                    AnswerAssembler.toAnswer(question, request)
                }.message shouldBe "문제에 대한 응답이 비어 있습니다."
            }
        }

        every { request.answers.isEmpty() } returns false
        When("응답이 비어있지 않은 경우") {
            And("글로 답하는 형식인 경우") {
                listOf(QuestionType.SHORT_ANSWER, QuestionType.LONG_ANSWER).forEach { type ->
                    every { question.type } returns type

                    And("응답이 하나가 아닌 경우") {
                        every { request.answers.size } returns 2

                        Then("예외를 반환한다.") {
                            shouldThrow<BadRequestException> {
                                AnswerAssembler.toAnswer(question, request)
                            }.message shouldBe "텍스트 응답은 하나만 제출할 수 있습니다."
                        }
                    }
                }
            }

            And("단일 선택형인 경우") {
                listOf(QuestionType.SINGLE_CHOICE, QuestionType.TRUE_OR_FALSE).forEach { type ->
                    every { question.type } returns type

                    And("응답이 하나가 아닌 경우") {
                        every { request.answers.size } returns 2

                        Then("예외를 반환한다.") {
                            shouldThrow<BadRequestException> {
                                AnswerAssembler.toAnswer(question, request)
                            }.message shouldBe "응답은 하나만 제출할 수 있습니다."
                        }
                    }

                    And("응답이 하나인 경우") {
                        every { request.answers.size } returns 1
                        every { question.options } returns listOf(
                            QuestionOption(id = 1, text = "choice1", isCorrect = false),
                            QuestionOption(id = 2, text = "choice2", isCorrect = true),
                        )
                        every { request.answers } returns listOf("non-existent choice")

                        Then("존재하지 않는 선택지인 경우 예외를 반환한다.") {
                            shouldThrow<BadRequestException> {
                                AnswerAssembler.toAnswer(question, request)
                            }.message shouldBe "존재하지 않는 선택지입니다."
                        }
                    }
                }
            }

            And("복수 선택형인 경우") {
                every { question.type } returns QuestionType.MULTIPLE_CHOICE

                And("존재하지 않는 선택지인 경우") {
                    every { question.options } returns listOf(
                        QuestionOption(id = 1, text = "choice1", isCorrect = false),
                        QuestionOption(id = 2, text = "choice2", isCorrect = true),
                    )
                    every { request.answers } returns listOf("non-existent choice")

                    Then("존재하지 않는 선택지인 경우 예외를 반환한다.") {
                        shouldThrow<BadRequestException> {
                            AnswerAssembler.toAnswer(question, request)
                        }.message shouldBe "존재하지 않는 선택지입니다."
                    }
                }
            }
        }
    }
})