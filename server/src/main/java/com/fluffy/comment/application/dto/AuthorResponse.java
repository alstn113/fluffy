package com.fluffy.comment.application.dto;

import com.fluffy.auth.domain.Member;

public record AuthorResponse(
        Long id,
        String name,
        String avatarUrl
) {

    public static AuthorResponse from(Member member) {
        return new AuthorResponse(
                member.getId(),
                member.getName(),
                member.getAvatarUrl()
        );
    }
}
