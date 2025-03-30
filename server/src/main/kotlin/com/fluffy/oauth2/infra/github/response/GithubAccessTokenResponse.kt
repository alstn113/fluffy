package com.fluffy.oauth2.infra.github.response

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(SnakeCaseStrategy::class)
data class GithubAccessTokenResponse(
    val accessToken: String,
    val tokenType: String,
    val scope: String
)
