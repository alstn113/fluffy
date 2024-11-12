package com.pass.oauth2.infra.github;

import com.pass.auth.domain.OAuth2Provider;
import com.pass.oauth2.application.OAuth2Strategy;
import com.pass.oauth2.domain.OAuth2UserInfo;

public class GithubOAuth2Strategy implements OAuth2Strategy {

    @Override
    public String buildOAuth2LoginUrl(String next) {
        return "";
    }

    @Override
    public OAuth2UserInfo fetchOAuth2UserInfo(String code) {
        return null;
    }

    @Override
    public String buildClientRedirectUrl(String next) {
        return "";
    }

    @Override
    public OAuth2Provider getProvider() {
        return null;
    }
}
