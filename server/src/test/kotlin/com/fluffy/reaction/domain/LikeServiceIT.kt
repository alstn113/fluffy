package com.fluffy.reaction.domain

import com.fluffy.auth.domain.MemberRepository
import com.fluffy.auth.fixture.MemberFixture
import com.fluffy.support.AbstractIntegrationTest
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class LikeServiceIT(
    private val likeService: LikeService,
    private val memberRepository: MemberRepository,
    private val reactionRepository: ReactionRepository
) : AbstractIntegrationTest() {

    @Test
    fun `좋아요를 누르면 좋아요가 생성된다`() {
        // given
        val member = memberRepository.save(MemberFixture.create())
        val like = Like(LikeTarget.EXAM, 1L)

        // when
        val reactionId = likeService.like(like, member.id)

        // then
        val reaction = reactionRepository.findAll()
        reaction.size shouldBe 1
        reaction[0].id shouldBe reactionId
        reaction[0].targetType shouldBe like.target.name
        reaction[0].targetId shouldBe like.targetId
        reaction[0].memberId shouldBe member.id
        reaction[0].type shouldBe ReactionType.LIKE
        reaction[0].status shouldBe ReactionStatus.ACTIVE
    }

    @Test
    fun `비활성화된 좋아요를 누르면 좋아요가 활성화된다`() {
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
        val response = likeService.like(like, member.id)

        // then
        val reaction = reactionRepository.findAll()

        reaction.size shouldBe 1
        response shouldBe reaction[0].id
        reaction[0].targetType shouldBe like.target.name
        reaction[0].targetId shouldBe like.targetId
        reaction[0].memberId shouldBe member.id
        reaction[0].type shouldBe ReactionType.LIKE
        reaction[0].status shouldBe ReactionStatus.ACTIVE
    }

    @Test
    fun `이전에 좋아요를 한 경우, 좋아요를 취소하면 좋아요가 삭제된다`() {
        // given
        val member = memberRepository.save(MemberFixture.create())
        val like = Like(LikeTarget.EXAM, 1L)
        val reaction = reactionRepository.save(
            Reaction.create(
                targetType = like.target.name,
                targetId = like.targetId,
                memberId = member.id,
                type = ReactionType.LIKE
            )
        )

        // when
        val response = likeService.removeLike(like, member.id)

        // then
        val reactionList = reactionRepository.findAll()
        response shouldBe reaction.id
        reactionList.size shouldBe 1
        reactionList[0].id shouldBe reaction.id
        reactionList[0].targetType shouldBe like.target.name
        reactionList[0].targetId shouldBe like.targetId
        reactionList[0].memberId shouldBe member.id
        reactionList[0].type shouldBe ReactionType.LIKE
        reactionList[0].status shouldBe ReactionStatus.DELETED
    }
}