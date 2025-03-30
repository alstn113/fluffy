package com.fluffy.exam.application

import com.fluffy.global.web.Accessor
import com.fluffy.reaction.domain.LikeService
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class ExamLikeServiceTest : BehaviorSpec({

    val likeService = mockk<LikeService>()
    val examLikeService = ExamLikeService(likeService)

    Given("like") {
        val examId = 1L
        val accessor = Accessor(1L)
        val expected = 1L

        When("좋아요를 추가할 때") {
            every { likeService.like(any(), any()) } returns expected

            val actual = examLikeService.like(examId, accessor)

            Then("좋아요를 추가하고 좋아요 식별자를 반환한다.") {
                actual shouldBe expected
            }
        }
    }

    Given("unlike") {
        val examId = 1L
        val accessor = Accessor(1L)
        val expected = 1L

        When("좋아요를 제거할 때") {
            every { likeService.removeLike(any(), any()) } returns expected

            val actual = examLikeService.unlike(examId, accessor)

            Then("좋아요를 제거하고 좋아요 식별자를 반환한다.") {
                actual shouldBe expected
            }
        }
    }
})
