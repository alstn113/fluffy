package com.fluffy.exam.domain.dto;

import com.querydsl.core.annotations.QueryProjection;

public record AuthorDto(
        Long id,
        String name,
        String avatarUrl
) {

    @QueryProjection
    public AuthorDto {
    }
}