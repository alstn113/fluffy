package com.fluffy.oauth2.ui

import com.fluffy.auth.application.response.TokenResponse
import com.fluffy.support.AbstractDocumentTest
import io.mockk.every
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.ResponseCookie
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.request.RequestDocumentation.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class OAuth2DocumentTest : AbstractDocumentTest() {

    @Test
    @DisplayName("OAuth2 로그인 페이지로 리다이렉트 된다.")
    fun oauth2Redirect() {
        val redirectUrl =
            "https://github.com/login/oauth/authorize?client_id={CLIENT_ID}&redirect_uri={REDIRECT_URI}&next=/"
        every { oauth2Service.getOAuth2LoginUrl(any(), any()) } returns redirectUrl

        mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/v1/auth/oauth2/redirect/{provider}", "github")
                .param("next", "/")
        )
            .andExpectAll(
                status().is3xxRedirection,
                header().string("Location", redirectUrl)
            )
            .andDo(
                restDocs.document(
                    pathParameters(
                        parameterWithName("provider").description("OAuth2 제공자")
                    ),
                    queryParameters(
                        parameterWithName("next").description("리다이렉트 후 이동할 URL").optional()
                    )
                )
            )
    }

    @Test
    @DisplayName("OAuth2 로그인 콜백 페이지로 리다이렉트 된다.")
    fun oauth2Callback() {
        val token = TokenResponse("access_token_value")
        val cookie = ResponseCookie.from("access_token", "access_token_value")
            .httpOnly(true)
            .secure(true)
            .domain(".fluffy.run")
            .path("/")
            .maxAge(60 * 60 * 24 * 30)
            .build()

        every { oauth2Service.oauth2Login(any(), any()) } returns token
        every { cookieManager.createAccessTokenCookie(any()) } returns cookie
        every { oauth2Service.getClientRedirectUrl(any(), any()) } returns "https://fluffy.run/{NEXT}"

        mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/v1/auth/oauth2/callback/{provider}", "github")
                .param("code", "{CODE}")
                .param("next", "{NEXT}")
        )
            .andExpectAll(
                status().is3xxRedirection,
                header().string("Location", "https://fluffy.run/{NEXT}"),
                header().exists("Set-Cookie")
            )
            .andDo(
                restDocs.document(
                    pathParameters(
                        parameterWithName("provider").description("OAuth2 제공자")
                    ),
                    queryParameters(
                        parameterWithName("code").description("인증 코드"),
                        parameterWithName("next").description("리다이렉트 후 이동할 URL").optional()
                    )
                )
            )
    }
}
