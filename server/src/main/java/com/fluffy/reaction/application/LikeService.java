package com.fluffy.reaction.application;

import com.fluffy.global.exception.BadRequestException;
import com.fluffy.reaction.domain.Reaction;
import com.fluffy.reaction.domain.ReactionRepository;
import com.fluffy.reaction.domain.ReactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final ReactionRepository reactionRepository;

    @Transactional
    public Long like(Like like, Long memberId) {
        String targetType = like.target().name();
        Long targetId = like.targetId();

        Reaction reaction = reactionRepository.findByTargetTypeAndTargetIdAndMemberId(targetType, targetId, memberId)
                .orElse(reactionRepository.save(new Reaction(targetType, targetId, memberId, ReactionType.LIKE)));

        reaction.active();

        return reaction.getId();
    }

    @Transactional
    public Long removeLike(Like like, Long memberId) {
        String targetType = like.target().name();
        Long targetId = like.targetId();

        Reaction reaction = reactionRepository.findByTargetTypeAndTargetIdAndMemberId(targetType, targetId, memberId)
                .orElseThrow(() -> new BadRequestException("좋아요를 한 상태가 아닙니다."));

        reaction.delete();

        return reaction.getId();
    }
}
