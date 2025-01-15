package com.fluffy.reaction.domain;

import com.fluffy.exam.domain.ExamRepositoryCustom;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReactionRepository extends JpaRepository<Reaction, Long>, ExamRepositoryCustom {

    Optional<Reaction> findByTargetAndTargetIdAndMemberId(ReactionTarget target, Long targetId, Long memberId);
}
