package com.fluffy.oauth2.infra.google

import com.fluffy.global.exception.InternalServerErrorException
import com.fluffy.oauth2.infra.google.request.GoogleAccessTokenRequest
import com.fluffy.oauth2.infra.google.response.GoogleAccessTokenResponse
import com.fluffy.oauth2.infra.google.response.GoogleUserInfoResponse
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.client.ClientHttpRequestFactory
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import java.time.Duration

@Component
class GoogleOAuth2Client(
    private val properties: GoogleOAuth2Properties,
    restClientBuilder: RestClient.Builder,
) {

    private val restClient = createRestClient(restClientBuilder)

    private fun createRestClient(restClientBuilder: RestClient.Builder): RestClient {
        return restClientBuilder.requestFactory(clientHttpRequestFactory()).build()
    }

    private fun clientHttpRequestFactory(): ClientHttpRequestFactory {
        val settings = ClientHttpRequestFactorySettings.defaults()
            .withConnectTimeout(Duration.ofSeconds(3))
            .withReadTimeout(Duration.ofSeconds(5))

        return ClientHttpRequestFactoryBuilder.simple().build(settings)
    }

    fun fetchAccessToken(code: String): GoogleAccessTokenResponse {
        val request = GoogleAccessTokenRequest(
            code = code,
            clientId = properties.clientId,
            clientSecret = properties.clientSecret,
            grantType = "authorization_code",
            redirectUri = properties.redirectUri,
        )

        return restClient.post()
            .uri("https://oauth2.googleapis.com/token")
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(request)
            .retrieve()
            .body(GoogleAccessTokenResponse::class.java)
            ?: throw InternalServerErrorException("구글 OAuth2 토큰 발급에 실패했습니다.")
    }

    fun fetchUserInfo(accessToken: String): GoogleUserInfoResponse {
        return restClient.get()
            .uri("https://www.googleapis.com/oauth2/v2/userinfo")
            .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
            .accept(APPLICATION_JSON)
            .retrieve()
            .body(GoogleUserInfoResponse::class.java)
            ?: throw InternalServerErrorException("구글 OAuth2 사용자 정보 조회에 실패했습니다.")
    }
}