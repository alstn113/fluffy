package com.fluffy.exam.domain

import com.fluffy.global.exception.BadRequestException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class ExamTest : BehaviorSpec({

    Given("시험이 생성되었을 때") {
        val exam = Exam.create(title = "시험 제목", memberId = 1L)

        Then("시험의 기본 상태가 올바르게 설정된다") {
            exam.title shouldBe "시험 제목"
            exam.description shouldBe ""
            exam.status shouldBe ExamStatus.DRAFT
            exam.memberId shouldBe 1L
            exam.isSingleAttempt shouldBe false
            exam.questions.size shouldBe 0
        }
    }

    Given("시험이 생성된 상태에서") {
        val exam = Exam.create(title = "시험 제목", memberId = 1L)

        When("시험 문제들을 변경하면") {
            val questions = listOf(
                Question.shortAnswer("문제 1", "지문", "정답"),
                Question.shortAnswer("문제 2", "지문", "정답")
            )
            exam.updateQuestions(questions)

            Then("문제들이 정상적으로 변경된다.") {
                exam.questions.size shouldBe 2
                exam.questions[0].text shouldBe "문제 1"
                exam.questions[1].text shouldBe "문제 2"
            }
        }

        When("기존 문제들을 새로운 문제들로 변경하면") {
            val initialQuestions = listOf(
                Question.shortAnswer("문제 1", "지문", "정답"),
                Question.shortAnswer("문제 2", "지문", "정답")
            )
            exam.updateQuestions(initialQuestions)

            val newQuestions = listOf(
                Question.shortAnswer("새 문제 1", "지문", "정답"),
                Question.shortAnswer("새 문제 2", "지문", "정답")
            )
            exam.updateQuestions(newQuestions)

            Then("기존 문제들은 삭제되고 새로운 문제들로 대체된다") {
                exam.questions.size shouldBe 2
                exam.questions[0].text shouldBe "새 문제 1"
                exam.questions[1].text shouldBe "새 문제 2"
            }
        }

        When("문제 개수가 200개를 초과하면") {
            val questions = List(201) { Question.shortAnswer("문제 $it", "지문", "정답") }

            Then("예외가 발생한다") {
                shouldThrow<BadRequestException> {
                    exam.updateQuestions(questions)
                }.message shouldBe "시험 문제는 200개 이하여야 합니다."
            }
        }
    }

    Given("출시되지 않은 시험이 있을 때") {
        val exam = Exam.create(title = "시험 제목", memberId = 1L)
        exam.updateQuestions(listOf(Question.shortAnswer("문제", "지문", "정답")))

        When("시험을 출시하면") {
            exam.publish()

            Then("시험의 상태가 출제됨 상태가 된다") {
                exam.status shouldBe ExamStatus.PUBLISHED
            }
        }
    }

    Given("이미 출시된 시험이 있을 때") {
        val exam = Exam.create(title = "시험 제목", memberId = 1L)
        exam.updateQuestions(listOf(Question.shortAnswer("문제", "지문", "정답")))
        exam.publish()

        When("시험을 다시 출시하려고 하면") {
            Then("예외가 발생한다") {
                shouldThrow<BadRequestException> {
                    exam.publish()
                }.message shouldBe "시험은 이미 출시되었습니다."
            }
        }

        When("제목을 수정하려고 하면") {
            Then("예외가 발생한다") {
                shouldThrow<BadRequestException> {
                    exam.updateTitle("새로운 제목")
                }.message shouldBe "시험이 출시된 후에는 제목을 수정할 수 없습니다."
            }
        }

        When("설명을 수정하려고 하면") {
            Then("예외가 발생한다") {
                shouldThrow<BadRequestException> {
                    exam.updateDescription("새로운 설명")
                }.message shouldBe "시험이 출시된 후에는 설명을 수정할 수 없습니다."
            }
        }

        When("응시 횟수 제한을 수정하려고 하면") {
            Then("예외가 발생한다") {
                shouldThrow<BadRequestException> {
                    exam.updateIsSingleAttempt(true)
                }.message shouldBe "시험이 출시된 후에는 응시 횟수 제한을 수정할 수 없습니다."
            }
        }
    }
})
