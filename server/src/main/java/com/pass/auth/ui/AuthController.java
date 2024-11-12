package com.pass.auth.ui;

import com.pass.auth.application.AuthService;
import com.pass.auth.application.dto.MyInfoResponse;
import com.pass.global.web.Accessor;
import com.pass.global.web.Auth;
import com.pass.global.web.cookie.CookieManager;
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

    @GetMapping("/api/v1/auth/mine")
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
