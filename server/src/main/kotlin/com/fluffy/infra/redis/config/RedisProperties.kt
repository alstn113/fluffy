package com.fluffy.infra.redis.config

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.data.redis")
data class RedisProperties(
    @field:NotBlank val host: String,
    @field:NotNull val port: Int,
    @field:NotNull val ssl: SSL,
) {

    data class SSL(
        @field:NotNull val enabled: Boolean,
    )
}
