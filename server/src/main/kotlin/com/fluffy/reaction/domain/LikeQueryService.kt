package com.fluffy.reaction.domain

import com.fluffy.global.web.Accessor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LikeQueryService(
    private val reactionRepository: ReactionRepository,
) {

    @Transactional(readOnly = true)
    fun isLiked(like: Like, accessor: Accessor): Boolean {
        if (accessor.isGuest()) return false

        return reactionRepository.findByTargetTypeAndTargetIdAndMemberIdAndType(
            like.target.name,
            like.targetId,
            accessor.id,
            ReactionType.LIKE,
        )?.status == ReactionStatus.ACTIVE
    }
}
