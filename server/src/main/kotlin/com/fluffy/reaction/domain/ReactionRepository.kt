package com.fluffy.reaction.domain

import org.springframework.data.jpa.repository.JpaRepository

interface ReactionRepository : JpaRepository<Reaction, Long> {

    fun findByTargetTypeAndTargetIdAndMemberIdAndType(
        targetType: String,
        targetId: Long,
        memberId: Long,
        type: ReactionType
    ): Reaction?
}