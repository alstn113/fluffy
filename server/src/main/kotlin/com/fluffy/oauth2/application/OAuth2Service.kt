package com.fluffy.oauth2.application

import com.fluffy.auth.application.AuthService
import com.fluffy.auth.application.response.TokenResponse
import com.fluffy.auth.domain.OAuth2Provider
import org.springframework.stereotype.Service

@Service
class OAuth2Service(
    private val oauth2Context: OAuth2Context,
    private val authService: AuthService,
) {

    fun getOAuth2LoginUrl(provider: OAuth2Provider, next: String): String {
        return oauth2Context.getOAuth2LoginUrl(provider, next)
    }

    fun oauth2Login(provider: OAuth2Provider, code: String): TokenResponse {
        val userInfo = oauth2Context.getOAuth2UserInfo(provider, code)

        return authService.createToken(userInfo, provider)
    }

    fun getClientRedirectUrl(provider: OAuth2Provider, next: String): String {
        return oauth2Context.getClientRedirectUrl(provider, next)
    }
}