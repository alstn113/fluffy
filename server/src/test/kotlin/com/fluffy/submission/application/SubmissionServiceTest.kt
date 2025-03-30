package com.fluffy.submission.application

import com.fluffy.exam.domain.ExamRepository
import com.fluffy.exam.fixture.ExamFixture
import com.fluffy.global.exception.BadRequestException
import com.fluffy.global.web.Accessor
import com.fluffy.submission.application.request.QuestionResponseRequest
import com.fluffy.submission.application.request.SubmissionRequest
import com.fluffy.submission.domain.Submission
import com.fluffy.submission.domain.SubmissionRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*
import org.springframework.data.repository.findByIdOrNull

class SubmissionServiceTest : BehaviorSpec({

    val examRepository = mockk<ExamRepository>()
    val submissionRepository = mockk<SubmissionRepository>()
    val submissionLockService = mockk<SubmissionLockService>()
    val submissionService = SubmissionService(examRepository, submissionRepository, submissionLockService)

    Given("submit") {
        val memberId = 1L
        val examId = 1L
        val accessor = Accessor(memberId)
        val request = SubmissionRequest(
            accessor = accessor,
            examId = examId,
            questionResponses = listOf(
                QuestionResponseRequest(answers = listOf("답변 1")),
            ),
        )
        val exam = spyk(ExamFixture.create(id = examId))
        every { examRepository.findByIdOrNull(any()) } returns exam

        When("시험이 출제되지 않은 경우") {
            every { exam.isNotPublished() } returns true

            Then("예외를 발생시킨다.") {
                shouldThrow<BadRequestException> {
                    submissionService.submit(request)
                }.message shouldBe "시험이 공개되지 않았습니다."
            }
        }

        When("시험이 출제된 경우") {
            every { exam.isNotPublished() } returns false

            And("시험이 단일 제출만 허용하는 경우") {
                clearMocks(submissionLockService, submissionRepository)
                every { exam.isSingleAttempt } returns true
                every { submissionLockService.submitWithLock(any(), any(), any()) } returns Unit

                submissionService.submit(request)

                Then("분산락을 사용하여 제출한다.") {
                    verify(exactly = 1) { submissionLockService.submitWithLock(any(), any(), any()) }
                    verify(exactly = 0) { submissionRepository.save(any()) }
                }
            }

            And("시험이 단일 제출이 아닌 경우") {
                clearMocks(submissionLockService, submissionRepository)
                mockkObject(SubmissionAssembler)
                every { SubmissionAssembler.toSubmission(any(), any(), any()) } returns mockk()

                val submission = mockk<Submission>()
                every { exam.isSingleAttempt } returns false
                every { submissionRepository.save(any()) } returns submission

                submissionService.submit(request)

                Then("제출을 한다.") {
                    verify(exactly = 1) { submissionRepository.save(any()) }
                    verify(exactly = 0) { submissionLockService.submitWithLock(any(), any(), any()) }
                }

                unmockkObject(SubmissionAssembler)
            }
        }
    }
})
