package com.fluffy.auth.domain;

import com.fluffy.global.exception.NotFoundException;
import java.util.Arrays;

public enum OAuth2Provider {

    GITHUB,
    GOOGLE,
    ;

    public static OAuth2Provider from(String provider) {
        return Arrays.stream(values())
                .filter(it -> it.name().equalsIgnoreCase(provider))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("존재하지 않는 OAuth2 제공자입니다. 제공자: " + provider));
    }
}
