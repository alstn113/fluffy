package com.fluffy.submission.application

import com.fluffy.exam.fixture.ExamFixture
import com.fluffy.global.exception.BadRequestException
import com.fluffy.global.web.Accessor
import com.fluffy.submission.application.request.QuestionResponseRequest
import com.fluffy.submission.application.request.SubmissionRequest
import com.fluffy.submission.domain.SubmissionRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*

class SubmissionLockServiceTest : BehaviorSpec({

    val submissionRepository = mockk<SubmissionRepository>()
    val submissionLockService = SubmissionLockService(submissionRepository)

    Given("submitWithLock") {
        val exam = ExamFixture.create(id = 1L)
        val request = SubmissionRequest(
            examId = exam.id,
            questionResponses = listOf(
                QuestionResponseRequest(
                    answers = listOf("answer1")
                )
            ),
            accessor = Accessor(1L)
        )
        val lockName = "lockName"

        When("한 번도 제출하지 않은 경우") {
            mockkObject(SubmissionAssembler)
            every { submissionRepository.existsByExamIdAndMemberId(any(), any()) } returns false
            every { SubmissionAssembler.toSubmission(any(), any(), any()) } returns mockk()
            every { submissionRepository.save(any()) } returns mockk()

            submissionLockService.submitWithLock(request, exam, lockName)

            Then("제출이 성공한다.") {
                verify(exactly = 1) { submissionRepository.save(any()) }
            }

            unmockkObject(SubmissionAssembler)
        }

        When("이미 제출한 경우") {
            every { submissionRepository.existsByExamIdAndMemberId(any(), any()) } returns true

            Then("예외를 발생시킨다.") {
                shouldThrow<BadRequestException> {
                    submissionLockService.submitWithLock(request, exam, lockName)
                }.message shouldBe "한 번만 제출 가능합니다."
            }
        }
    }
})