package com.fluffy.submission.domain.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ParticipantDto {

    private Long id;
    private String name;
    private String email;
    private String avatarUrl;

    @QueryProjection
    public ParticipantDto(Long id, String name, String email, String avatarUrl) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.avatarUrl = avatarUrl;
    }
}