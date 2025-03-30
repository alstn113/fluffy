package com.fluffy.reaction.domain

import com.fluffy.infra.persistence.AuditableEntity
import jakarta.persistence.*

@Entity
class Reaction(
    targetType: String,
    targetId: Long,
    memberId: Long,
    type: ReactionType,
    status: ReactionStatus,
    id: Long = 0,
) : AuditableEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = id
        protected set

    @field:Column(nullable = false)
    var targetType: String = targetType
        protected set

    @field:Column(nullable = false)
    var targetId: Long = targetId
        protected set

    @field:Column(nullable = false)
    var memberId: Long = memberId
        protected set

    @field:Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var type: ReactionType = type
        protected set

    @field:Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var status: ReactionStatus = status
        protected set

    companion object {

        fun create(targetType: String, targetId: Long, memberId: Long, type: ReactionType): Reaction {
            return Reaction(
                targetType = targetType,
                targetId = targetId,
                memberId = memberId,
                type = type,
                status = ReactionStatus.ACTIVE
            )
        }
    }

    fun activate() {
        this.status = ReactionStatus.ACTIVE
    }


    fun delete() {
        this.status = ReactionStatus.DELETED
    }
}