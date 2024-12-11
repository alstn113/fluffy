package com.fluffy.oauth2.infra.google.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(SnakeCaseStrategy.class)
public record GoogleAccessTokenRequest(
        String code,
        String clientId,
        String clientSecret,
        String grantType,
        String redirectUri
) {
}
