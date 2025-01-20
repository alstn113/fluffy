package com.fluffy.reaction.domain;

import com.fluffy.global.persistence.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "unique_reaction", columnNames = {"target_type", "target_id", "member_id", "type"})
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Reaction extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String targetType;

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

    public Reaction(String targetType, Long targetId, Long memberId, ReactionType type) {
        this(null, targetType, targetId, memberId, type, ReactionStatus.ACTIVE);
    }

    public Reaction(
            Long id,
            String targetType,
            Long targetId,
            Long memberId,
            ReactionType type,
            ReactionStatus status
    ) {
        this.id = id;
        this.targetType = targetType;
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
