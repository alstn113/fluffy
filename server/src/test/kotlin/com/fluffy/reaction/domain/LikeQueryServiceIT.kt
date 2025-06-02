package com.fluffy.reaction.domain

import com.fluffy.auth.domain.MemberRepository
import com.fluffy.auth.fixture.MemberFixture
import com.fluffy.global.web.Accessor
import com.fluffy.support.AbstractIntegrationTest
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class LikeQueryServiceIT(
    private val likeQueryService: LikeQueryService,
    private val memberRepository: MemberRepository,
    private val reactionRepository: ReactionRepository,
) : AbstractIntegrationTest() {

    @Test
    fun `사용자의 좋아요 여부 조회 시, 이전에 좋아요를 누르지 않았다면 false를 반환한다`() {
        // given
        val member = memberRepository.save(MemberFixture.create())
        val like = Like(LikeTarget.EXAM, 1L)
        reactionRepository.save(
            Reaction.create(
                targetType = like.target.name,
                targetId = like.targetId,
                memberId = member.id,
                type = ReactionType.LIKE
            )
        )

        // when
        val response = likeQueryService.isLiked(like, Accessor(member.id))

        // then
        response shouldBe true
    }

    @Test
    fun `사용자의 좋아요 여부 조회 시, 이전에 좋아요를 눌렀다면 false를 반환한다`() {
        // given
        val member = memberRepository.save(MemberFixture.create())
        val like = Like(LikeTarget.EXAM, 1L)
        reactionRepository.save(
            Reaction.create(
                targetType = like.target.name,
                targetId = like.targetId,
                memberId = member.id,
                type = ReactionType.LIKE
            ).apply { delete() }
        )

        // when
        val response = likeQueryService.isLiked(like, Accessor(member.id))

        // then
        response shouldBe false
    }
}