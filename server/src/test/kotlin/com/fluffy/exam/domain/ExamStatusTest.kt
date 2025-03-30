package com.fluffy.exam.domain

import com.fluffy.global.exception.NotFoundException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ExamStatusTest : StringSpec({

    "존재하는 시험 상태는 정상적으로 반환된다." {
        // given
        val status = "PUBLISHED"

        // when
        val examStatus = ExamStatus.from(status)

        // then
        examStatus shouldBe ExamStatus.PUBLISHED
    }

    "대소문자 구분 없이 정상적으로 반환된다." {
        // given
        val status = "published"

        // when
        val examStatus = ExamStatus.from(status)

        // then
        examStatus shouldBe ExamStatus.PUBLISHED
    }

    "존재하지 않는 시험 상태는 예외를 던진다." {
        // given
        val invalidStatus = "INVALID_STATUS"

        // when & then
        shouldThrow<NotFoundException> { ExamStatus.from(invalidStatus) }
            .message shouldBe "존재하지 않는 시험 상태입니다. 시험 상태: $invalidStatus"
    }
})
