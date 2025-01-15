package com.fluffy.reaction.domain;

import com.fluffy.global.persistence.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Reaction extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReactionTarget target;

    @Column(nullable = false)
    private Long targetId;

    @Column(nullable = false)
    private Long memberId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReactionType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReactionStatus status;

    public Reaction(ReactionTarget target, Long targetId, Long memberId, ReactionType type) {
        this(null, target, targetId, memberId, type, ReactionStatus.ACTIVE);
    }

    public Reaction(
            Long id,
            ReactionTarget target,
            Long targetId,
            Long memberId,
            ReactionType type,
            ReactionStatus status
    ) {
        this.id = id;
        this.target = target;
        this.targetId = targetId;
        this.memberId = memberId;
        this.type = type;
        this.status = status;
    }

    public void active() {
        this.status = ReactionStatus.ACTIVE;
    }

    public void delete() {
        this.status = ReactionStatus.DELETED;
    }
}
