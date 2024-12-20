package com.fluffy.oauth2.api;

import com.fluffy.auth.domain.OAuth2Provider;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class OAuth2ProviderConverter implements Converter<String, OAuth2Provider> {

    @Override
    public OAuth2Provider convert(@NonNull String source) {
        return OAuth2Provider.from(source);
    }
}
