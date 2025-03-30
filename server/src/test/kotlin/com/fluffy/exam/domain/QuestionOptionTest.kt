package com.fluffy.exam.domain

import com.fluffy.global.exception.BadRequestException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe

class QuestionOptionTest : StringSpec({

    "문제 선택지가 정상적으로 생성된다." {
        // given
        val text = "a".repeat(200)
        val isCorrect = true

        // when
        val questionOption = QuestionOption(text, isCorrect)

        // then
        questionOption.text shouldBe text
    }

    "문제 선택지의 내용은 비어있을 수 없다." {
        forAll(
            row(""),
            row(" ")
        ) {
            shouldThrow<BadRequestException> { QuestionOption(it, true) }
                .message shouldBe "문제 선택지의 내용은 비어있을 수 없습니다."
        }
    }

    "문제 선택지의 내용은 200자 이하여야 한다." {
        // given
        val text = "a".repeat(201)

        // when & then
        shouldThrow<BadRequestException> { QuestionOption(text, true) }
            .message shouldBe "문제 선택지의 내용은 200자 이하여야 합니다."
    }
})
