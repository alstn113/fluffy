package com.pass.auth.ui;

import com.pass.global.web.cookie.CookieProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final CookieProvider cookieProvider;

    @PostMapping("/api/v1/auth/logout")
    public ResponseEntity<Void> logout() {
        ResponseCookie expiredAccessTokenCookie = cookieProvider.createExpiredAccessTokenCookie();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, expiredAccessTokenCookie.toString())
                .build();
    }
}
