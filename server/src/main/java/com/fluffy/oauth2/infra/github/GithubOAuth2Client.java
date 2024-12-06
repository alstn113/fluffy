package com.fluffy.oauth2.infra.github;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.fluffy.oauth2.infra.github.dto.GithubAccessTokenRequest;
import com.fluffy.oauth2.infra.github.dto.GithubAccessTokenResponse;
import com.fluffy.oauth2.infra.github.dto.GithubUserInfoResponse;
import java.time.Duration;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class GithubOAuth2Client {

    private final GithubOAuth2Properties properties;
    private final RestClient restClient;

    public GithubOAuth2Client(GithubOAuth2Properties properties, RestClient.Builder restClientBuilder) {
        this.properties = properties;
        this.restClient = createRestClient(restClientBuilder);
    }

    private RestClient createRestClient(RestClient.Builder restClientBuilder) {
        return restClientBuilder.requestFactory(clientHttpRequestFactory()).build();
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() {
        ClientHttpRequestFactorySettings settings = ClientHttpRequestFactorySettings.DEFAULTS
                .withConnectTimeout(Duration.ofSeconds(3))
                .withReadTimeout(Duration.ofSeconds(5));

        return ClientHttpRequestFactories.get(settings);
    }

    public GithubAccessTokenResponse fetchAccessToken(String code) {
        GithubAccessTokenRequest request = new GithubAccessTokenRequest(
                code,
                properties.clientId(),
                properties.clientSecret()
        );

        return restClient.post()
                .uri("https://github.com/login/oauth/access_token")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(GithubAccessTokenResponse.class);
    }

    public GithubUserInfoResponse fetchUserInfo(String accessToken) {
        return restClient.get()
                .uri("https://api.github.com/user")
                .header(AUTHORIZATION, String.format("Bearer %s", accessToken))
                .accept(APPLICATION_JSON)
                .retrieve()
                .body(GithubUserInfoResponse.class);
    }
}
