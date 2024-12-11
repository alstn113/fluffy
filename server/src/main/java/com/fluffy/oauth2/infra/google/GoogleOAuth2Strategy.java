package com.fluffy.oauth2.infra.google;

import com.fluffy.auth.domain.OAuth2Provider;
import com.fluffy.oauth2.application.OAuth2Strategy;
import com.fluffy.oauth2.domain.OAuth2UserInfo;
import com.fluffy.oauth2.infra.google.dto.GoogleAccessTokenResponse;
import com.fluffy.oauth2.infra.google.dto.GoogleUserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class GoogleOAuth2Strategy implements OAuth2Strategy {

    private final GoogleOAuth2Client googleOAuth2Client;
    private final GoogleOAuth2Properties properties;

    @Override
    public String buildOAuth2LoginUrl(String next) {
        return UriComponentsBuilder.fromHttpUrl("https://accounts.google.com/o/oauth2/v2/auth")
                .queryParam("client_id", properties.clientId())
                .queryParam("redirect_uri", properties.redirectUri())
                .queryParam("response_type", "code")
                .queryParam("scope", "email profile")
                .build()
                .toUriString();
    }

    @Override
    public OAuth2UserInfo fetchOAuth2UserInfo(String code) {
        GoogleAccessTokenResponse accessTokenResponse = googleOAuth2Client.fetchAccessToken(code);
        String accessToken = accessTokenResponse.accessToken();

        GoogleUserInfoResponse userInfoResponse = googleOAuth2Client.fetchUserInfo(accessToken);

        return userInfoResponse.toOAuth2UserInfo();
    }

    @Override
    public String buildClientRedirectUrl(String next) {
        return UriComponentsBuilder.fromHttpUrl(properties.clientUri())
                .path(next)
                .build()
                .toUriString();
    }

    @Override
    public OAuth2Provider getProvider() {
        return OAuth2Provider.GOOGLE;
    }
}
