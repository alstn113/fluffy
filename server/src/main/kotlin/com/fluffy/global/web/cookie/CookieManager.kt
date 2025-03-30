package com.fluffy.global.web.cookie

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Component

@Component
class CookieManager(
    private val cookieProperties: CookieProperties,
) {

    fun createAccessTokenCookie(accessToken: String): ResponseCookie {
        return ResponseCookie.from(cookieProperties.accessTokenKey, accessToken)
            .httpOnly(cookieProperties.httpOnly)
            .secure(cookieProperties.secure)
            .domain(cookieProperties.domain)
            .path(cookieProperties.path)
            .maxAge(cookieProperties.maxAge)
            .build()
    }

    fun createExpiredAccessTokenCookie(): ResponseCookie {
        return ResponseCookie.from(cookieProperties.accessTokenKey, "")
            .httpOnly(cookieProperties.httpOnly)
            .secure(cookieProperties.secure)
            .domain(cookieProperties.domain)
            .path(cookieProperties.path)
            .maxAge(0)
            .build()
    }

    fun extractAccessToken(request: HttpServletRequest): String? {
        val tokenCookieName = cookieProperties.accessTokenKey
        val cookies = request.cookies

        if (cookies.isNullOrEmpty()) {
            return null
        }

        return cookies.firstOrNull { it.name == tokenCookieName }?.value

    }
}