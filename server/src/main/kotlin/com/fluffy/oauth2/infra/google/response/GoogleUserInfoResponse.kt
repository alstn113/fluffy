package com.fluffy.oauth2.infra.google.response

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.fluffy.oauth2.domain.OAuth2UserInfo

@JsonNaming(SnakeCaseStrategy::class)
data class GoogleUserInfoResponse(
    val id: String,
    val email: String,
    val name: String,
    val picture: String
) {

    fun toOAuth2UserInfo(): OAuth2UserInfo {
        return OAuth2UserInfo(
            id,
            name,
            email,
            picture
        )
    }
}