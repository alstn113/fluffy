package com.fluffy.oauth2.infra.github

import com.fluffy.global.exception.InternalServerErrorException
import com.fluffy.oauth2.infra.github.request.GithubAccessTokenRequest
import com.fluffy.oauth2.infra.github.response.GithubAccessTokenResponse
import com.fluffy.oauth2.infra.github.response.GithubUserInfoResponse
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.client.ClientHttpRequestFactory
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import java.time.Duration

@Component
class GithubOAuth2Client(
    private val properties: GithubOAuth2Properties,
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

    fun fetchAccessToken(code: String): GithubAccessTokenResponse {
        val request = GithubAccessTokenRequest(
            code = code,
            clientId = properties.clientId,
            clientSecret = properties.clientSecret,
        )

        return restClient.post()
            .uri("https://github.com/login/oauth/access_token")
            .contentType(APPLICATION_JSON)
            .accept(APPLICATION_JSON)
            .body(request)
            .retrieve()
            .body(GithubAccessTokenResponse::class.java)
            ?: throw InternalServerErrorException("Github OAuth2 토큰 발급에 실패했습니다.")
    }

    fun fetchUserInfo(accessToken: String): GithubUserInfoResponse {
        return restClient.get()
            .uri("https://api.github.com/user")
            .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", accessToken))
            .accept(APPLICATION_JSON)
            .retrieve()
            .body(GithubUserInfoResponse::class.java)
            ?: throw InternalServerErrorException("Github 사용자 정보 조회에 실패했습니다.")
    }
}