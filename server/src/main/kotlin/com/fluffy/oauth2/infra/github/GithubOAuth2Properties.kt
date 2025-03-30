package com.fluffy.oauth2.infra.github

import jakarta.validation.constraints.NotBlank
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("auth.oauth2.github")
data class GithubOAuth2Properties(
    @field:NotBlank val clientId: String,
    @field:NotBlank val clientSecret: String,
    @field:NotBlank val redirectUri: String,
    @field:NotBlank val clientUri: String
)
