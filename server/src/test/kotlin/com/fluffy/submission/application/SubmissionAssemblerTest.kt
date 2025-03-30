package com.fluffy.submission.application

import com.fluffy.exam.domain.Exam
import com.fluffy.global.exception.BadRequestException
import com.fluffy.submission.application.request.SubmissionRequest
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class SubmissionAssemblerTest : BehaviorSpec({

    Given("toSubmission") {
        val exam = mockk<Exam>()
        val memberId = 1L
        val request = mockk<SubmissionRequest>()

        every { exam.questions } returns mockk()
        every { request.questionResponses } returns mockk()

        When("문제와 응답의 크기가 일치하지 않는 경우") {
            val questionSize = 3
            val questionResponseSize = 2

            every { exam.questions.size } returns questionSize
            every { request.questionResponses.size } returns questionResponseSize

            Then("예외를 반환한다.") {
                shouldThrow<BadRequestException> {
                    SubmissionAssembler.toSubmission(exam, memberId, request)
                }.message shouldBe "문제들에 대한 응답의 크기가 일치하지 않습니다. 문제 크기: $questionSize, 응답 크기: $questionResponseSize"
            }
        }
    }
})