package com.fluffy.submission.application

import com.fluffy.auth.domain.MemberRepository
import com.fluffy.exam.domain.Exam
import com.fluffy.exam.domain.ExamRepository
import com.fluffy.exam.domain.findByIdOrThrow
import com.fluffy.global.exception.ForbiddenException
import com.fluffy.global.web.Accessor
import com.fluffy.submission.domain.Submission
import com.fluffy.submission.domain.SubmissionRepository
import com.fluffy.submission.domain.findByIdOrThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class SubmissionQueryServiceTest : BehaviorSpec({

    val examRepository = mockk<ExamRepository>()
    val submissionRepository = mockk<SubmissionRepository>()
    val memberRepository = mockk<MemberRepository>()
    val submissionQueryService = SubmissionQueryService(
        examRepository = examRepository,
        submissionRepository = submissionRepository,
        memberRepository = memberRepository,
    )

    Given("getSummariesByExamId") {
        val examId = 1L
        val memberId = 1L
        val accessor = Accessor(memberId)

        val exam = mockk<Exam>()
        every { examRepository.findByIdOrThrow(examId) } returns exam

        When("사용자와 시험 작성자가 다른 경우") {
            every { exam.isNotWrittenBy(any()) } returns true

            Then("예외를 발생시킨다.") {
                shouldThrow<ForbiddenException> {
                    submissionQueryService.getSummariesByExamId(
                        examId = examId,
                        accessor = accessor,
                    )
                }.message shouldBe "해당 시험 제출 목록을 조회할 권한이 없습니다."
            }
        }
    }

    Given("getDetail") {
        val examId = 1L
        val submissionId = 1L
        val memberId = 1L
        val accessor = Accessor(memberId)

        val exam = mockk<Exam>()
        val submission = mockk<Submission>()

        every { examRepository.findByIdOrThrow(examId) } returns exam
        every { submissionRepository.findByIdOrThrow(submissionId) } returns submission

        When("시험 출제자도 아니고, 시험 제출자도 아닌 경우") {
            every { exam.isNotWrittenBy(any()) } returns true
            every { submission.isNotWrittenBy(any()) } returns true

            Then("예외를 발생시킨다.") {
                shouldThrow<ForbiddenException> {
                    submissionQueryService.getDetail(
                        examId = examId,
                        submissionId = submissionId,
                        accessor = accessor,
                    )
                }.message shouldBe "해당 시험 제출을 조회할 권한이 없습니다."
            }
        }
    }
})