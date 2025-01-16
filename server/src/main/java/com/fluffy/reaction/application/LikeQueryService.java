package com.fluffy.reaction.application;

import com.fluffy.reaction.domain.ReactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeQueryService {

    private final ReactionRepository reactionRepository;
}
