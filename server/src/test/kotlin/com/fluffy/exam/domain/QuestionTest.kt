package com.fluffy.exam.domain

import com.fluffy.global.exception.BadRequestException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe

class QuestionTest : StringSpec({

    "문제의 지문은 비어있을 수 있다" {
        val question = Question.shortAnswer(
            text = "단답형 문제",
            passage = "",
            correctAnswer = "정답"
        )

        question.passage shouldBe ""
    }

    "문제의 질문은 1~2000자여야 한다" {
        forAll(
            row(""),
            row(" "),
            row("a".repeat(2001))
        ) { invalidText ->
            shouldThrow<BadRequestException> {
                Question(
                    text = invalidText,
                    passage = "지문",
                    type = QuestionType.SHORT_ANSWER,
                    correctAnswer = "정답"
                )
            }.message shouldBe "문제의 질문은 1~2000자여야 합니다."
        }
    }

    "단답형 문제가 정상적으로 생성된다." {
        val question = Question.shortAnswer(
            text = "단답형 문제",
            passage = "지문",
            correctAnswer = "정답"
        )

        question.type shouldBe QuestionType.SHORT_ANSWER
        question.correctAnswer shouldBe "정답"
    }

    "단답형 문제의 정답 길이가 1~200자여야 한다" {
        forAll(
            row(""),
            row(" "),
            row("a".repeat(201))
        ) { invalidAnswer ->
            shouldThrow<BadRequestException> {
                Question.shortAnswer(
                    text = "단답형 문제",
                    passage = "단답형 지문",
                    correctAnswer = invalidAnswer
                )
            }.message shouldBe "단답형 문제의 정답은 1~200자여야 합니다."
        }
    }

    "서술형 문제가 정상적으로 생성된다." {
        val question = Question.longAnswer(
            text = "서술형 문제",
            passage = "서술형 지문"
        )

        question.type shouldBe QuestionType.LONG_ANSWER
        question.correctAnswer shouldBe null
    }

    "중복된 문제 선택지의 내용은 허용되지 않는다" {
        val optionText = "선택지 1"
        val duplicateOptions = listOf(
            QuestionOption(text = optionText, isCorrect = true),
            QuestionOption(text = optionText, isCorrect = false)
        )

        shouldThrow<BadRequestException> {
            Question.singleChoice(
                text = "객관식 단일 선택",
                passage = "객관식 지문",
                options = duplicateOptions
            )
        }.message shouldBe "중복된 문제 선택지의 내용은 허용되지 않습니다."
    }

    "문제 선택지는 2~10개여야 한다." {
        forAll(
            row(listOf(QuestionOption(text = "Option", isCorrect = true))), // 1개
            row(List(11) { QuestionOption(text = "Option ${it + 1}", isCorrect = it == 0) }) // 11개
        ) { invalidOptions ->
            shouldThrow<BadRequestException> {
                Question.singleChoice(
                    text = "객관식 단일 선택",
                    passage = "객관식 지문",
                    options = invalidOptions
                )
            }.message shouldBe "문제 선택지는 2~10개여야 합니다."
        }
    }

    "객관식 단일 선택 문제가 정상적으로 생성된다" {
        val options = listOf(
            QuestionOption(text = "Option 1", isCorrect = true),
            QuestionOption(text = "Option 2", isCorrect = false)
        )

        val question = Question.singleChoice(
            text = "객관식 단일 선택",
            passage = "객관식 지문",
            options = options
        )

        question.type shouldBe QuestionType.SINGLE_CHOICE
        question.options.size shouldBe 2
    }

    "객관식 단일 선택 정답은 1개여야 한다" {
        forAll(
            row(
                listOf(
                    QuestionOption(text = "Option 1", isCorrect = false),
                    QuestionOption(text = "Option 2", isCorrect = false)
                )
            ),
            row(
                listOf(
                    QuestionOption(text = "Option 1", isCorrect = true),
                    QuestionOption(text = "Option 2", isCorrect = true)
                )
            )
        ) { invalidOptions ->
            shouldThrow<BadRequestException> {
                Question.singleChoice(
                    text = "객관식 단일 선택",
                    passage = "객관식 지문",
                    options = invalidOptions
                )
            }.message shouldBe "객관식 단일 선택의 정답은 1개여야 합니다."
        }
    }

    "객관식 복수 선택 문제가 정상적으로 생성된다." {
        val options = listOf(
            QuestionOption(text = "Option 1", isCorrect = true),
            QuestionOption(text = "Option 2", isCorrect = false),
            QuestionOption(text = "Option 3", isCorrect = false)
        )

        val question = Question.multipleChoice(
            text = "객관식 복수 선택",
            passage = "객관식 지문",
            options = options
        )

        question.type shouldBe QuestionType.MULTIPLE_CHOICE
        question.options.size shouldBe 3
        question.options.count { it.isCorrect } shouldBe 1
    }

    "객관식 복수 선택 정답은 여러 개일 수 있다" {
        val options = listOf(
            QuestionOption(text = "Option 1", isCorrect = true),
            QuestionOption(text = "Option 2", isCorrect = false),
            QuestionOption(text = "Option 3", isCorrect = true)
        )

        val question = Question.multipleChoice(
            text = "객관식 다중 선택",
            passage = "객관식 지문",
            options = options
        )

        question.type shouldBe QuestionType.MULTIPLE_CHOICE
        question.options.size shouldBe 3
        question.options.count { it.isCorrect } shouldBe 2
    }

    "객관식 복수 선택 문제는 정답이 없을 수 있다." {
        val options = listOf(
            QuestionOption(text = "Option 1", isCorrect = false),
            QuestionOption(text = "Option 2", isCorrect = false),
            QuestionOption(text = "Option 3", isCorrect = false)
        )

        val question = Question.multipleChoice(
            text = "객관식 다중 선택",
            passage = "객관식 지문",
            options = options
        )

        question.type shouldBe QuestionType.MULTIPLE_CHOICE
        question.options.size shouldBe 3
        question.options.count { it.isCorrect } shouldBe 0
    }

    "참/거짓 문제가 정상적으로 생성된다" {
        val question = Question.trueOrFalse(
            text = "이 문제는 참인가요?",
            passage = "참/거짓 지문",
            trueOrFalse = true
        )

        question.type shouldBe QuestionType.TRUE_OR_FALSE
        question.options.size shouldBe 2
        question.options[0].text shouldBe "TRUE"
        question.options[0].isCorrect shouldBe true
        question.options[1].text shouldBe "FALSE"
        question.options[1].isCorrect shouldBe false
    }
})
