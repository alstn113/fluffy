package com.fluffy.reaction.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeQueryService {

    private final ReactionRepository reactionRepository;

    @Transactional(readOnly = true)
    public boolean isLiked(Like like, Long memberId) {
        if (memberId == null || memberId == -1) {
            return false;
        }

        return reactionRepository.existsByTargetTypeAndTargetIdAndMemberId(
                like.target().name(),
                like.targetId(),
                memberId
        );
    }
}
