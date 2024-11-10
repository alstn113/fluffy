package com.pass.auth.ui;

import com.pass.global.web.cookie.CookieProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.pass.auth.ui.dto.LoginWebRequest;
import com.pass.auth.ui.dto.SignupWebRequest;
import com.pass.auth.application.AuthService;
import com.pass.auth.application.dto.TokenResponse;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final CookieProvider cookieProvider;

    @PostMapping("/api/v1/auth/signup")
    public ResponseEntity<Void> signup(@RequestBody @Valid SignupWebRequest request) {
        authService.signup(request.toAppRequest());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/v1/auth/login")
    public ResponseEntity<Void> login(@RequestBody @Valid LoginWebRequest request) {
        TokenResponse token = authService.login(request.toAppRequest());
        ResponseCookie accessTokenCookie = cookieProvider.createAccessTokenCookie(token.accessToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .build();
    }

    @PostMapping("/api/v1/auth/logout")
    public ResponseEntity<Void> logout() {
        ResponseCookie expiredAccessTokenCookie = cookieProvider.createExpiredAccessTokenCookie();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, expiredAccessTokenCookie.toString())
                .build();
    }
}
