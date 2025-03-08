package com.fluffy.oauth2.infra.google;

import com.fluffy.oauth2.infra.google.dto.GoogleAccessTokenRequest;
import com.fluffy.oauth2.infra.google.dto.GoogleAccessTokenResponse;
import com.fluffy.oauth2.infra.google.dto.GoogleUserInfoResponse;
import java.time.Duration;
import org.springframework.boot.web.client.ClientHttpRequestFactories;
import org.springframework.boot.web.client.ClientHttpRequestFactorySettings;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class GoogleOAuth2Client {

    private final GoogleOAuth2Properties properties;
    private final RestClient restClient;

    public GoogleOAuth2Client(GoogleOAuth2Properties properties, RestClient.Builder restClientBuilder) {
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

    public GoogleAccessTokenResponse fetchAccessToken(String code) {
        GoogleAccessTokenRequest request = new GoogleAccessTokenRequest(
                code,
                properties.clientId(),
                properties.clientSecret(),
                "authorization_code",
                properties.redirectUri()
        );

        return restClient.post()
                .uri("https://oauth2.googleapis.com/token")
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(GoogleAccessTokenResponse.class);
    }

    public GoogleUserInfoResponse fetchUserInfo(String accessToken) {
        return restClient.get()
                .uri("https://www.googleapis.com/oauth2/v2/userinfo")
                .header(AUTHORIZATION, String.format("Bearer %s", accessToken))
                .accept(APPLICATION_JSON)
                .retrieve()
                .body(GoogleUserInfoResponse.class);
    }
}
