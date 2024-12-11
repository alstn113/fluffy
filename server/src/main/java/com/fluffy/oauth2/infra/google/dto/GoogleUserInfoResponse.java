package com.fluffy.oauth2.infra.google.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fluffy.oauth2.domain.OAuth2UserInfo;

@JsonNaming(SnakeCaseStrategy.class)
public record GoogleUserInfoResponse(
        String id,
        String email,
        String name,
        String picture
) {

    public OAuth2UserInfo toOAuth2UserInfo() {
        return new OAuth2UserInfo(
                id,
                name,
                email,
                picture
        );
    }
}
