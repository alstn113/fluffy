package com.fluffy.oauth2.infra.github.request

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(SnakeCaseStrategy::class)
data class GithubAccessTokenRequest(
    val clientId: String,
    val clientSecret: String,
    val code: String,
)
