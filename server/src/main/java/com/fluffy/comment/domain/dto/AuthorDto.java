package com.fluffy.comment.domain.dto;

import com.querydsl.core.annotations.QueryProjection;

public record AuthorDto(
        Long id,
        String name,
        String avatarUrl
) {

    public static final AuthorDto EMPTY = new AuthorDto(0L, "", "");

    @QueryProjection
    public AuthorDto {
    }
}