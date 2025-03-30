package com.fluffy.reaction.domain

import com.fluffy.global.exception.NotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class LikeService(
    private val reactionRepository: ReactionRepository
) {

    @Transactional
    fun like(like: Like, memberId: Long): Long {
        val targetType = like.target.name
        val targetId = like.targetId

        val reaction = reactionRepository
            .findByTargetTypeAndTargetIdAndMemberIdAndType(targetType, targetId, memberId, ReactionType.LIKE)
            ?: reactionRepository.save(Reaction.create(targetType, targetId, memberId, ReactionType.LIKE))

        reaction.activate()

        return reaction.id
    }

    @Transactional
    fun removeLike(like: Like, memberId: Long): Long {
        val targetType = like.target.name
        val targetId = like.targetId

        val reaction = reactionRepository
            .findByTargetTypeAndTargetIdAndMemberIdAndType(targetType, targetId, memberId, ReactionType.LIKE)
            ?: throw NotFoundException("아직 좋아요를 하지 않았습니다. 좋아요 정보: $like")

        reaction.delete()

        return reaction.id
    }
}
