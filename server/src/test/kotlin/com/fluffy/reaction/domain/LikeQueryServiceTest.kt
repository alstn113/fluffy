package com.fluffy.reaction.domain

import com.fluffy.global.web.Accessor
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class LikeQueryServiceTest : BehaviorSpec({

    val reactionRepository = mockk<ReactionRepository>()
    val likeQueryService = LikeQueryService(reactionRepository)

    Given("isLiked") {
        val like = Like(LikeTarget.EXAM, 1L)
        val memberId = 1L

        When("사용자가 비회원인 경우") {
            val accessor = Accessor.GUEST
            val actual = likeQueryService.isLiked(like, accessor)

            Then("거짓을 반환한다.") {
                actual shouldBe false
            }
        }

        When("사용자가 회원인 경우") {
            val accessor = Accessor(memberId)

            And("좋아요가 활성 상태인 경우") {
                val reaction = Reaction(
                    targetType = like.target.name,
                    targetId = like.targetId,
                    memberId = memberId,
                    type = ReactionType.LIKE,
                    status = ReactionStatus.ACTIVE
                )

                every {
                    reactionRepository.findByTargetTypeAndTargetIdAndMemberIdAndType(any(), any(), any(), any())
                } returns reaction

                val actual = likeQueryService.isLiked(like, accessor)

                Then("참을 반환한다.") {
                    actual shouldBe true
                }
            }

            And("좋아요가 삭제 상태인 경우") {
                val reaction = Reaction(
                    targetType = like.target.name,
                    targetId = like.targetId,
                    memberId = memberId,
                    type = ReactionType.LIKE,
                    status = ReactionStatus.DELETED
                )

                every {
                    reactionRepository.findByTargetTypeAndTargetIdAndMemberIdAndType(any(), any(), any(), any())
                } returns reaction

                val actual = likeQueryService.isLiked(like, accessor)

                Then("거짓을 반환한다.") {
                    actual shouldBe false
                }
            }

            And("좋아요가 존재하지 않는 경우") {
                every {
                    reactionRepository.findByTargetTypeAndTargetIdAndMemberIdAndType(any(), any(), any(), any())
                } returns null

                val actual = likeQueryService.isLiked(like, accessor)

                Then("거짓을 반환한다.") {
                    actual shouldBe false
                }
            }
        }
    }
})
