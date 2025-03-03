package com.fluffy.submission.domain.dto;

import com.querydsl.core.annotations.QueryProjection;

public record ParticipantDto(
        Long id,
        String name,
        String email,
        String avatarUrl
) {

    @QueryProjection
    public ParticipantDto {
    }
}