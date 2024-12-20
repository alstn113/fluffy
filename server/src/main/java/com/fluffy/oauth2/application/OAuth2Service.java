package com.fluffy.oauth2.application;

import com.fluffy.auth.application.AuthService;
import com.fluffy.auth.application.response.TokenResponse;
import com.fluffy.auth.domain.OAuth2Provider;
import com.fluffy.oauth2.domain.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuth2Service {

    private final OAuth2Context oauth2Context;
    private final AuthService authService;

    public String getOAuth2LoginUrl(OAuth2Provider provider, String next) {
        return oauth2Context.getOAuth2LoginUrl(provider, next);
    }

    public TokenResponse oauth2Login(OAuth2Provider provider, String code) {
        OAuth2UserInfo userInfo = oauth2Context.getOAuth2UserInfo(provider, code);

        return authService.createToken(userInfo, provider);
    }

    public String getClientRedirectUrl(OAuth2Provider provider, String next) {
        return oauth2Context.getClientRedirectUrl(provider, next);
    }
}
