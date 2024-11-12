package com.pass.oauth2.application;

import com.pass.auth.domain.OAuth2Provider;
import com.pass.oauth2.application.exception.OAuth2ProviderNotFoundException;
import com.pass.oauth2.domain.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2Context {

    private final OAuth2StrategyRegistry oauth2StrategyRegistry;

    public String getOAuth2LoginUrl(OAuth2Provider provider, String next) {
        OAuth2Strategy strategy = getOAuth2Strategy(provider);
        return strategy.buildOAuth2LoginUrl(next);
    }

    public OAuth2UserInfo getOAuth2UserInfo(OAuth2Provider provider, String code) {
        OAuth2Strategy strategy = getOAuth2Strategy(provider);
        return strategy.fetchOAuth2UserInfo(code);
    }

    public String getClientRedirectUrl(OAuth2Provider provider, String next) {
        OAuth2Strategy strategy = getOAuth2Strategy(provider);
        return strategy.buildClientRedirectUrl(next);
    }

    private OAuth2Strategy getOAuth2Strategy(OAuth2Provider provider) {
        return oauth2StrategyRegistry.getOAuth2Strategy(provider)
                .orElseThrow(() -> new OAuth2ProviderNotFoundException(provider));
    }
}
