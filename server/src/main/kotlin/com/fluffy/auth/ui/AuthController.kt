package com.fluffy.auth.ui

import com.fluffy.auth.application.AuthService
import com.fluffy.auth.application.response.MyInfoResponse
import com.fluffy.global.web.Accessor
import com.fluffy.global.web.Auth
import com.fluffy.global.web.cookie.CookieManager
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController


@RestController
class AuthController(
    private val authService: AuthService,
    private val cookieManager: CookieManager
) {
    @GetMapping("/api/v1/auth/me")
    fun getMyInfo(@Auth accessor: Accessor): ResponseEntity<MyInfoResponse> {
        val response: MyInfoResponse = authService.getMyInfo(accessor.id)

        return ResponseEntity.ok(response)
    }

    @PostMapping("/api/v1/auth/logout")
    fun logout(): ResponseEntity<Unit> {
        val expiredAccessTokenCookie = cookieManager.createExpiredAccessTokenCookie()

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, expiredAccessTokenCookie.toString())
            .build()
    }
}
