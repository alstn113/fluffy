package com.fluffy.oauth2.infra.google

import com.fluffy.auth.domain.OAuth2Provider
import com.fluffy.oauth2.application.OAuth2Strategy
import com.fluffy.oauth2.domain.OAuth2UserInfo
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder

@Component
class GoogleOAuth2Strategy(
    private val googleOAuth2Client: GoogleOAuth2Client,
    private val properties: GoogleOAuth2Properties,
) : OAuth2Strategy {

    override fun buildOAuth2LoginUrl(next: String): String {
        return UriComponentsBuilder.fromUriString("https://accounts.google.com/o/oauth2/v2/auth")
            .queryParam("client_id", properties.clientId)
            .queryParam("redirect_uri", properties.redirectUri)
            .queryParam("response_type", "code")
            .queryParam("scope", "email profile")
            .build()
            .toUriString()
    }

    override fun fetchOAuth2UserInfo(code: String): OAuth2UserInfo {
        val accessTokenResponse = googleOAuth2Client.fetchAccessToken(code)
        val accessToken = accessTokenResponse.accessToken

        val userInfoResponse = googleOAuth2Client.fetchUserInfo(accessToken)

        return userInfoResponse.toOAuth2UserInfo()
    }

    override fun buildClientRedirectUrl(next: String): String {
        return UriComponentsBuilder.fromUriString(properties.clientUri)
            .path(next)
            .build()
            .toUriString()
    }

    override fun getProvider(): OAuth2Provider {
        return OAuth2Provider.GOOGLE
    }
}