package com.fluffy.auth.ui;

import com.fluffy.auth.application.AuthService;
import com.fluffy.auth.application.response.MyInfoResponse;
import com.fluffy.global.web.Accessor;
import com.fluffy.global.web.Auth;
import com.fluffy.global.web.cookie.CookieManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final CookieManager cookieManager;

    @GetMapping("/api/v1/auth/me")
    public ResponseEntity<MyInfoResponse> getMyInfo(
            @Auth Accessor accessor
    ) {
        MyInfoResponse response = authService.getMyInfo(accessor.id());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/v1/auth/logout")
    public ResponseEntity<Void> logout() {
        ResponseCookie expiredAccessTokenCookie = cookieManager.createExpiredAccessTokenCookie();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, expiredAccessTokenCookie.toString())
                .build();
    }
}
