package com.fluffy.auth.infra

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "auth.jwt")
data class JwtTokenProperties(

    @field:NotBlank
    val secretKey: String,

    @field:NotNull
    @field:Positive
    val expirationTime: Long,
)
