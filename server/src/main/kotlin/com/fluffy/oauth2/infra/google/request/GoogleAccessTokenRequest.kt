package com.fluffy.oauth2.infra.google.request

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(SnakeCaseStrategy::class)
data class GoogleAccessTokenRequest(
    val code: String,
    val clientId: String,
    val clientSecret: String,
    val grantType: String,
    val redirectUri: String,
)
