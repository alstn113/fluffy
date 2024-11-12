package com.pass.oauth2.application;

import com.pass.auth.domain.OAuth2Provider;
import com.pass.oauth2.domain.OAuth2UserInfo;

public interface OAuth2Strategy {

    String buildOAuth2LoginUrl(String next);

    OAuth2UserInfo fetchOAuth2UserInfo(String code);

    String buildClientRedirectUrl(String next);

    OAuth2Provider getProvider();
}
