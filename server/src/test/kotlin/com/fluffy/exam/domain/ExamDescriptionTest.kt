package com.fluffy.exam.domain

import com.fluffy.global.exception.BadRequestException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ExamDescriptionTest : StringSpec({

    "시험 설명이 정상적으로 생성된다." {
        // given
        val description = "a".repeat(2000)

        // when
        val examDescription = ExamDescription(description)

        // then
        examDescription.value shouldBe description
    }

    "시험 설명은 비어있을 수 있다." {
        // given
        val description = ""

        // when
        val examDescription = ExamDescription(description)

        // then
        examDescription.value shouldBe description
    }

    "시험 설명은 2000자 이하여야 한다." {
        // given
        val description = "a".repeat(2001)

        // when & then
        shouldThrow<BadRequestException> { ExamDescription(description) }
            .message shouldBe "시험 설명은 2000자 이하여야 합니다."
    }
})