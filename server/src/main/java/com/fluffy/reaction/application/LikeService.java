package com.fluffy.reaction.application;

import com.fluffy.global.exception.NotFoundException;
import com.fluffy.reaction.domain.Reaction;
import com.fluffy.reaction.domain.ReactionRepository;
import com.fluffy.reaction.domain.ReactionTarget;
import com.fluffy.reaction.domain.ReactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final ReactionRepository reactionRepository;

    @Transactional
    public Long like(ReactionTarget target, Long targetId, Long memberId) {
        Reaction reaction = reactionRepository.findByTargetAndTargetIdAndMemberId(target, targetId, memberId)
                .orElse(reactionRepository.save(new Reaction(target, targetId, memberId, ReactionType.LIKE)));

        reaction.active();

        return reaction.getId();
    }

    @Transactional
    public Long removeLike(ReactionTarget target, Long targetId, Long memberId) {
        Reaction reaction = reactionRepository.findByTargetAndTargetIdAndMemberId(target, targetId, memberId)
                .orElseThrow(() -> new NotFoundException("좋아요를 한 상태가 아닙니다."));

        reaction.delete();

        return reaction.getId();
    }
}
