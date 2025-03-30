package com.fluffy.exam.domain

import com.fluffy.global.exception.BadRequestException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.data.forAll
import io.kotest.data.row
import io.kotest.matchers.shouldBe

class ExamTitleTest : StringSpec({

    "시험 제목이 정상적으로 생성된다." {
        // given
        val title = "a".repeat(100)

        // when
        val examTitle = ExamTitle(title)

        // then
        examTitle.value shouldBe title
    }

    "시험 제목은 비어있을 수 없다." {
        forAll(
            row(""),
            row(" ")
        ) {
            shouldThrow<BadRequestException> { ExamTitle(it) }
                .message shouldBe "시험 제목은 비어있을 수 없습니다."
        }
    }

    "시험 제목은 100자 이하여야 한다." {
        // given
        val title = "a".repeat(101)

        // when & then
        shouldThrow<BadRequestException> { ExamTitle(title) }
            .message shouldBe "시험 제목은 100자 이하여야 합니다."
    }
})

