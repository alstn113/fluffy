package com.fluffy.oauth2.application

import com.fluffy.auth.domain.OAuth2Provider
import org.springframework.stereotype.Component

@Component
class OAuth2StrategyRegistry(
    strategies: Set<OAuth2Strategy>,
) {

    private val strategies: Map<OAuth2Provider, OAuth2Strategy> = strategies.associateBy { it.getProvider() }

    fun getOAuth2Strategy(provider: OAuth2Provider): OAuth2Strategy? {
        return strategies[provider]
    }
}