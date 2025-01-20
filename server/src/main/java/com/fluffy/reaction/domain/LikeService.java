package com.fluffy.reaction.domain;

import com.fluffy.global.exception.BadRequestException;
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

        Reaction reaction = reactionRepository
                .findByTargetTypeAndTargetIdAndMemberIdAndType(targetType, targetId, memberId, ReactionType.LIKE)
                .orElseGet(() -> {
                    Reaction newReaction = new Reaction(targetType, targetId, memberId, ReactionType.LIKE);
                    return reactionRepository.save(newReaction);
                });

        reaction.active();

        return reaction.getId();
    }

    @Transactional
    public Long removeLike(Like like, Long memberId) {
        String targetType = like.target().name();
        Long targetId = like.targetId();

        Reaction reaction = reactionRepository
                .findByTargetTypeAndTargetIdAndMemberIdAndType(targetType, targetId, memberId, ReactionType.LIKE)
                .orElseThrow(() -> new BadRequestException("좋아요를 한 상태가 아닙니다."));

        reaction.delete();

        return reaction.getId();
    }
}
