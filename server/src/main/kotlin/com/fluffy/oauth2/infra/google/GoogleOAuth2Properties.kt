package com.fluffy.oauth2.infra.google

import jakarta.validation.constraints.NotBlank
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("auth.oauth2.google")
data class GoogleOAuth2Properties(
    @field:NotBlank val clientId: String,
    @field:NotBlank val clientSecret: String,
    @field:NotBlank val redirectUri: String,
    @field:NotBlank val clientUri: String,
)
