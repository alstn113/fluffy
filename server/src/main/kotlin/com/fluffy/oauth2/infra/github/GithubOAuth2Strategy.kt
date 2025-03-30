package com.fluffy.oauth2.infra.github

import com.fluffy.auth.domain.OAuth2Provider
import com.fluffy.oauth2.application.OAuth2Strategy
import com.fluffy.oauth2.domain.OAuth2UserInfo
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder

@Component
class GithubOAuth2Strategy(
    private val githubOAuth2Client: GithubOAuth2Client,
    private val properties: GithubOAuth2Properties
) : OAuth2Strategy {

    override fun buildOAuth2LoginUrl(next: String): String {
        val redirectUriWithNext = UriComponentsBuilder.fromUriString(properties.redirectUri)
            .queryParam("next", next)
            .build()
            .toUriString()

        return UriComponentsBuilder.fromUriString("https://github.com/login/oauth/authorize")
            .queryParam("client_id", properties.clientId)
            .queryParam("redirect_uri", redirectUriWithNext)
            .build()
            .toUriString()
    }

    override fun fetchOAuth2UserInfo(code: String): OAuth2UserInfo {
        val accessTokenResponse = githubOAuth2Client.fetchAccessToken(code)
        val accessToken: String = accessTokenResponse.accessToken

        val userInfoResponse = githubOAuth2Client.fetchUserInfo(accessToken)

        return userInfoResponse.toOAuth2UserInfo()
    }

    override fun buildClientRedirectUrl(next: String): String {
        return UriComponentsBuilder.fromUriString(properties.clientUri)
            .path(next)
            .build()
            .toUriString()
    }

    override fun getProvider(): OAuth2Provider {
        return OAuth2Provider.GITHUB
    }
}