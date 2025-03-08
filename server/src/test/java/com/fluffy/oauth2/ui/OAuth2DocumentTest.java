package com.fluffy.oauth2.ui;

import com.fluffy.auth.application.response.TokenResponse;
import com.fluffy.support.AbstractDocumentTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.http.ResponseCookie;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OAuth2DocumentTest extends AbstractDocumentTest {

    @Test
    @DisplayName("OAuth2 로그인 페이지로 리다이렉트 된다.")
    void oauth2Redirect() throws Exception {
        String redirectUrl = "https://github.com/login/oauth/authorize?client_id={CLIENT_ID}&redirect_uri={REDIRECT_URI}&next=/";
        when(oauth2Service.getOAuth2LoginUrl(any(), any()))
                .thenReturn(redirectUrl);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/auth/oauth2/redirect/{provider}", "github")
                        .param("next", "/")
                )
                .andExpectAll(
                        status().is3xxRedirection(),
                        header().string("Location", redirectUrl)
                )
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("provider").description("OAuth2 제공자")
                        ),
                        queryParameters(
                                parameterWithName("next").description("리다이렉트 후 이동할 URL").optional()
                        )
                ));
    }

    @Test
    @DisplayName("OAuth2 로그인 콜백 페이지로 리다이렉트 된다.")
    void oauth2Callback() throws Exception {
        when(oauth2Service.oauth2Login(any(), any()))
                .thenReturn(new TokenResponse("access_token_value"));
        when(cookieManager.createAccessTokenCookie(any()))
                .thenReturn(ResponseCookie.from("access_token", "access_token_value")
                        .httpOnly(true)
                        .secure(true)
                        .domain(".fluffy.run")
                        .path("/")
                        .maxAge(60 * 60 * 24 * 30)
                        .build());
        when(oauth2Service.getClientRedirectUrl(any(), any()))
                .thenReturn("https://fluffy.run/{NEXT}");

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/auth/oauth2/callback/{provider}", "github")
                        .param("code", "{CODE}")
                        .param("next", "{NEXT}")
                )
                .andExpectAll(
                        status().is3xxRedirection(),
                        header().string("Location", "https://fluffy.run/{NEXT}"),
                        header().exists("Set-Cookie")
                )
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("provider").description("OAuth2 제공자")
                        ),
                        queryParameters(
                                parameterWithName("code").description("인증 코드"),
                                parameterWithName("next").description("리다이렉트 후 이동할 URL").optional()
                        )
                ));
    }
}
