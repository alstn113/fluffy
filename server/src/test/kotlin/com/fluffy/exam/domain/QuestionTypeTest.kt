package com.fluffy.exam.domain

import com.fluffy.global.exception.NotFoundException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class QuestionTypeTest : StringSpec({

    "존재하는 문제 유형은 정상적으로 반환된다." {
        // given
        val value = "SINGLE_CHOICE"

        // when
        val questionType = QuestionType.from(value)

        // then
        questionType shouldBe QuestionType.SINGLE_CHOICE
    }

    "대소문자 구분 없이 정상적으로 반환된다." {
        // given
        val value = "true_or_false"

        // when
        val questionType = QuestionType.from(value)

        // then
        questionType shouldBe QuestionType.TRUE_OR_FALSE
    }

    "존재하지 않는 문제 유형은 예외를 던진다." {
        // given
        val invalidValue = "INVALID_QUESTION_TYPE"

        // when & then
        shouldThrow<NotFoundException> { QuestionType.from(invalidValue) }
            .message shouldBe "존재하지 않는 문제 유형입니다. 문제 유형: $invalidValue"
    }
})
