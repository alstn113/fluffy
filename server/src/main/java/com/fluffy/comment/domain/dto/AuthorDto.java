package com.fluffy.comment.domain.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthorDto {

    private Long id;
    private String name;
    private String avatarUrl;

    public static final AuthorDto EMPTY = new AuthorDto(-1L, "", "");

    @QueryProjection
    public AuthorDto(Long id, String name, String avatarUrl) {
        this.id = id;
        this.name = name;
        this.avatarUrl = avatarUrl;
    }
}