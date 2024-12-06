package com.fluffy.global.web.cookie;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "auth.cookie")
public record CookieProperties(
        String accessTokenKey,
        boolean httpOnly,
        boolean secure,
        String domain,
        String path,
        int maxAge
) {
}
