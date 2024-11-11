package com.pass.oauth2.application.exception;

import com.pass.global.exception.NotFoundException;
import com.pass.oauth2.domain.OAuth2Provider;

public class OAuth2ProviderNotFoundException extends NotFoundException {

    private static final String MESSAGE = "지원하지 않는 OAuth2 제공자입니다. (%s)";

    public OAuth2ProviderNotFoundException(OAuth2Provider provider) {
        super(String.format(MESSAGE, provider.name()));
    }
}
