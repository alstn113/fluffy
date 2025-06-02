package com.fluffy.oauth2.infra.github.response

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import com.fluffy.oauth2.domain.OAuth2UserInfo

@JsonNaming(SnakeCaseStrategy::class)
data class GithubUserInfoResponse(
    val id: String,
    val login: String,
    val avatarUrl: String,
    val email: String?,
) {

    fun toOAuth2UserInfo(): OAuth2UserInfo {
        return OAuth2UserInfo(
            id,
            login,
            email,
            avatarUrl,
        )
    }
}