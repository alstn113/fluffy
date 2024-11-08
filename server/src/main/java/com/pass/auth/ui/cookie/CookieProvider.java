package com.pass.auth.ui.cookie;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CookieProvider {

    private final CookieProperties cookieProperties;

    public ResponseCookie createAccessTokenCookie(String accessToken) {
        return ResponseCookie.from(cookieProperties.accessTokenKey(), accessToken)
                .httpOnly(cookieProperties.httpOnly())
                .secure(cookieProperties.secure())
                .domain(cookieProperties.domain())
                .maxAge(cookieProperties.maxAge())
                .build();
    }

    public ResponseCookie createExpiredAccessTokenCookie() {
        return ResponseCookie.from(cookieProperties.accessTokenKey(), "")
                .httpOnly(cookieProperties.httpOnly())
                .secure(cookieProperties.secure())
                .domain(cookieProperties.domain())
                .maxAge(0)
                .build();
    }

    @Nullable
    public String extractAccessTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        return Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(cookieProperties.accessTokenKey()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}
