package com.fluffy.infra.redis.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.data.redis")
public record RedisProperties(
        @NotBlank String host,
        @NotNull @Positive int port,
        SSL ssl
) {

    public record SSL(Boolean enabled) {
    }
}
