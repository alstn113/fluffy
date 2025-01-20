package com.fluffy.exam.application;

import com.fluffy.global.web.Accessor;
import com.fluffy.reaction.domain.Like;
import com.fluffy.reaction.domain.LikeService;
import com.fluffy.reaction.domain.LikeTarget;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExamLikeService {

    private final LikeService likeService;

    @Transactional
    public Long like(Long examId, Accessor accessor) {
        return likeService.like(new Like(LikeTarget.EXAM, examId), accessor.id());
    }

    @Transactional
    public Long unlike(Long examId, Accessor accessor) {
        return likeService.removeLike(new Like(LikeTarget.EXAM, examId), accessor.id());
    }
}
