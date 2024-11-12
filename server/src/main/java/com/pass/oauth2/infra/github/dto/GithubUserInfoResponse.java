package com.pass.oauth2.infra.github.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.pass.oauth2.domain.OAuth2UserInfo;
import jakarta.annotation.Nullable;

@JsonNaming(SnakeCaseStrategy.class)
public record GithubUserInfoResponse(
        Long socialId,
        String login,
        String avatarUrl,
        @Nullable String email
) {

    public OAuth2UserInfo toOAuth2UserInfo() {
        return new OAuth2UserInfo(
                socialId,
                login,
                email,
                avatarUrl
        );
    }
}
