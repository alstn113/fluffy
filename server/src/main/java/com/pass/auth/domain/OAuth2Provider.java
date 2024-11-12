package com.pass.auth.domain;

import com.pass.oauth2.domain.exception.OAuth2ProviderNotFoundException;
import java.util.Arrays;

public enum OAuth2Provider {

    GITHUB,
    ;

    public static OAuth2Provider from(String provider) {
        return Arrays.stream(values())
                .filter(it -> it.name().equalsIgnoreCase(provider))
                .findFirst()
                .orElseThrow(() -> new OAuth2ProviderNotFoundException(provider));
    }

}
