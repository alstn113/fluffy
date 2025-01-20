package com.fluffy.reaction.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {

    Optional<Reaction> findByTargetTypeAndTargetIdAndMemberIdAndType(
            String targetType,
            Long targetId,
            Long memberId,
            ReactionType type
    );
}
