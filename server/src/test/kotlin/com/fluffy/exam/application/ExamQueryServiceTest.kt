package com.fluffy.exam.application

import com.fluffy.exam.domain.Exam
import com.fluffy.exam.domain.ExamRepository
import com.fluffy.exam.domain.findByIdOrThrow
import com.fluffy.global.exception.ForbiddenException
import com.fluffy.global.exception.NotFoundException
import com.fluffy.global.web.Accessor
import com.fluffy.reaction.domain.LikeQueryService
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class ExamQueryServiceTest : BehaviorSpec({

    val examRepository = mockk<ExamRepository>()
    val likeQueryService = mockk<LikeQueryService>()
    val examQueryService = ExamQueryService(examRepository, likeQueryService)

    Given("getExamDetailSummary") {
        val examId = 1L
        val memberId = 1L
        val accessor = Accessor(memberId)

        When("시험이 존재하지 않는 경우") {
            every { examRepository.findExamDetailSummary(examId) } returns null

            Then("예외를 발생시킨다.") {
                shouldThrow<NotFoundException> {
                    examQueryService.getExamDetailSummary(examId, accessor)
                }.message shouldBe "존재하지 않는 시험입니다. 시험 식별자: $examId"
            }
        }
    }

    Given("getExamWithAnswers") {
        val examId = 1L
        val memberId = 1L
        val accessor = Accessor(memberId)

        When("사용자와 시험 작성자가 다른 경우") {
            val exam = mockk<Exam>()
            every { examRepository.findByIdOrThrow(examId) } returns exam
            every { exam.isNotWrittenBy(accessor.id) } returns true

            Then("예외를 발생시킨다.") {
                shouldThrow<ForbiddenException> {
                    examQueryService.getExamWithAnswers(examId, accessor)
                }.message shouldBe "해당 시험에 접근할 수 없습니다."
            }
        }
    }
})
