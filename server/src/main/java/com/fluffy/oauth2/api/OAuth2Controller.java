package com.fluffy.oauth2.api;

import com.fluffy.auth.application.response.TokenResponse;
import com.fluffy.auth.domain.OAuth2Provider;
import com.fluffy.global.web.cookie.CookieManager;
import com.fluffy.oauth2.application.OAuth2Service;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OAuth2Controller {

    private final OAuth2Service oauth2Service;
    private final CookieManager cookieManager;

    @GetMapping("/api/v1/auth/oauth2/redirect/{provider}")
    public void oauth2Redirect(
            @PathVariable OAuth2Provider provider,
            @RequestParam(value = "next", defaultValue = "/") String next,
            HttpServletResponse response
    ) throws IOException {
        String redirectUri = oauth2Service.getOAuth2LoginUrl(provider, next);

        response.sendRedirect(redirectUri);
    }

    @GetMapping("/api/v1/auth/oauth2/callback/{provider}")
    public void oauth2Callback(
            @PathVariable OAuth2Provider provider,
            @RequestParam("code") String code,
            @RequestParam(value = "next", defaultValue = "/") String next,
            HttpServletResponse response
    ) throws IOException {
        TokenResponse tokenResponse = oauth2Service.oauth2Login(provider, code);
        ResponseCookie accessTokenCookie = cookieManager.createAccessTokenCookie(tokenResponse.accessToken());

        String redirectUri = oauth2Service.getClientRedirectUrl(provider, next);

        response.setHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        response.sendRedirect(redirectUri);
    }
}
