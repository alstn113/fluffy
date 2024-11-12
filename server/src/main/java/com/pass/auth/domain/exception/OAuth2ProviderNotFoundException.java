package com.pass.auth.domain.exception;

import com.pass.global.exception.NotFoundException;

public class OAuth2ProviderNotFoundException extends NotFoundException {

    private static final String MESSAGE = "지원하지 않는 OAuth2 제공자입니다. (%s)";

    public OAuth2ProviderNotFoundException(String provider) {
        super(String.format(MESSAGE, provider));
    }
}
