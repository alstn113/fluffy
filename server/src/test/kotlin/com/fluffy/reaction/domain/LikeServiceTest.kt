package com.fluffy.reaction.domain

import com.fluffy.global.exception.NotFoundException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class LikeServiceTest : BehaviorSpec({

    val reactionRepository = mockk<ReactionRepository>()
    val likeService = LikeService(reactionRepository)

    Given("like") {
        val like = Like(LikeTarget.EXAM, 1L)
        val memberId = 1L
        val reaction = mockk<Reaction>()
        every { reaction.id } returns 1L
        every { reaction.activate() } returns Unit

        When("이미 좋아요가 존재하는 경우") {

            every {
                reactionRepository.findByTargetTypeAndTargetIdAndMemberIdAndType(any(), any(), any(), any())
            } returns reaction

            Then("좋아요를 활성화하고, 식별자를 반환한다.") {
                val actual = likeService.like(like, memberId)
                val expected = 1L

                verify { reaction.activate() }
                actual shouldBe expected
            }
        }

        When("좋아요가 존재하지 않는 경우") {
            every {
                reactionRepository.findByTargetTypeAndTargetIdAndMemberIdAndType(any(), any(), any(), any())
            } returns null
            every {
                reactionRepository.save(any())
            } returns reaction

            Then("좋아요를 생성하고, 식별자를 반환한다.") {
                val actual = likeService.like(like, memberId)
                val expected = 1L

                actual shouldBe expected
            }
        }
    }

    Given("removeLike") {
        val like = Like(LikeTarget.EXAM, 1L)
        val memberId = 1L
        val reaction = mockk<Reaction>()
        every { reaction.id } returns 1L
        every { reaction.delete() } returns Unit

        When("좋아요가 존재하는 경우") {
            every {
                reactionRepository.findByTargetTypeAndTargetIdAndMemberIdAndType(any(), any(), any(), any())
            } returns reaction

            Then("좋아요를 비활성화하고, 식별자를 반환한다.") {
                val actual = likeService.removeLike(like, memberId)
                val expected = 1L

                verify { reaction.delete() }
                actual shouldBe expected
            }
        }

        When("좋아요가 존재하지 않는 경우") {
            every {
                reactionRepository.findByTargetTypeAndTargetIdAndMemberIdAndType(any(), any(), any(), any())
            } returns null

            Then("예외를 발생시킨다.") {
                shouldThrow<NotFoundException> {
                    likeService.removeLike(like, memberId)
                }.message shouldBe "아직 좋아요를 하지 않았습니다. 좋아요 정보: $like"
            }
        }
    }
})
