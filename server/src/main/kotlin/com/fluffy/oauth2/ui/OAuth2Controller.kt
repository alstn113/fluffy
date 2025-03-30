package com.fluffy.oauth2.ui

import com.fluffy.auth.application.response.TokenResponse
import com.fluffy.auth.domain.OAuth2Provider
import com.fluffy.global.web.cookie.CookieManager
import com.fluffy.oauth2.application.OAuth2Service
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class OAuth2Controller(
    private val oauth2Service: OAuth2Service,
    private val cookieManager: CookieManager
) {

    @GetMapping("/api/v1/auth/oauth2/redirect/{provider}")
    fun oauth2Redirect(
        @PathVariable provider: OAuth2Provider,
        @RequestParam(value = "next", defaultValue = "/") next: String,
        response: HttpServletResponse
    ) {
        val redirectUrl = oauth2Service.getOAuth2LoginUrl(provider, next)

        response.sendRedirect(redirectUrl)
    }

    @GetMapping("/api/v1/auth/oauth2/callback/{provider}")
    fun oauth2Callback(
        @PathVariable provider: OAuth2Provider,
        @RequestParam("code") code: String,
        @RequestParam(value = "next", defaultValue = "/") next: String,
        response: HttpServletResponse
    ) {
        val tokenResponse: TokenResponse = oauth2Service.oauth2Login(provider, code)
        val accessTokenCookie = cookieManager.createAccessTokenCookie(tokenResponse.accessToken)

        val redirectUri = oauth2Service.getClientRedirectUrl(provider, next)

        response.setHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
        response.sendRedirect(redirectUri)
    }
}