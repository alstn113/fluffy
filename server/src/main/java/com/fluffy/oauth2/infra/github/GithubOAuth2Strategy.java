package com.fluffy.oauth2.infra.github;

import com.fluffy.auth.domain.OAuth2Provider;
import com.fluffy.oauth2.application.OAuth2Strategy;
import com.fluffy.oauth2.domain.OAuth2UserInfo;
import com.fluffy.oauth2.infra.github.dto.GithubAccessTokenResponse;
import com.fluffy.oauth2.infra.github.dto.GithubUserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class GithubOAuth2Strategy implements OAuth2Strategy {

    private final GithubOAuth2Client githubOAuth2Client;
    private final GithubOAuth2Properties properties;

    @Override
    public String buildOAuth2LoginUrl(String next) {
        String redirectUriWithNext = UriComponentsBuilder.fromHttpUrl(properties.redirectUri())
                .queryParam("next", next)
                .build()
                .toUriString();

        return UriComponentsBuilder.fromHttpUrl("https://github.com/login/oauth/authorize")
                .queryParam("client_id", properties.clientId())
                .queryParam("redirect_uri", redirectUriWithNext)
                .build()
                .toUriString();
    }

    @Override
    public OAuth2UserInfo fetchOAuth2UserInfo(String code) {
        GithubAccessTokenResponse accessTokenResponse = githubOAuth2Client.fetchAccessToken(code);
        String accessToken = accessTokenResponse.accessToken();

        GithubUserInfoResponse userInfoResponse = githubOAuth2Client.fetchUserInfo(accessToken);

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
        return OAuth2Provider.GITHUB;
    }
}
