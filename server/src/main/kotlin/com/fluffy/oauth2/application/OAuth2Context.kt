package com.fluffy.oauth2.application

import com.fluffy.auth.domain.OAuth2Provider
import com.fluffy.global.exception.NotFoundException
import com.fluffy.oauth2.domain.OAuth2UserInfo
import org.springframework.stereotype.Service

@Service
class OAuth2Context(
    private val oauth2StrategyRegistry: OAuth2StrategyRegistry,
) {

    fun getOAuth2LoginUrl(provider: OAuth2Provider, next: String): String {
        val strategy = getOAuth2Strategy(provider)

        return strategy.buildOAuth2LoginUrl(next)
    }

    fun getOAuth2UserInfo(provider: OAuth2Provider, code: String): OAuth2UserInfo {
        val strategy = getOAuth2Strategy(provider)

        return strategy.fetchOAuth2UserInfo(code)
    }

    fun getClientRedirectUrl(provider: OAuth2Provider, next: String): String {
        val strategy = getOAuth2Strategy(provider)

        return strategy.buildClientRedirectUrl(next)
    }

    private fun getOAuth2Strategy(provider: OAuth2Provider): OAuth2Strategy {
        return oauth2StrategyRegistry.getOAuth2Strategy(provider)
            ?: throw NotFoundException("지원하지 않은 OAuth2 제공자입니다. OAuth2 제공자: $provider")
    }
}