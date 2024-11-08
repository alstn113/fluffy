package com.pass.auth.infra;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "auth.jwt")
public record JwtTokenProperties(
        @NotBlank
        String secretKey,

        @NotNull
        @Positive
        Long expirationTime
) {
}
