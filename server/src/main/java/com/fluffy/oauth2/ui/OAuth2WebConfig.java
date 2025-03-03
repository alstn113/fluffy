package com.fluffy.oauth2.ui;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class OAuth2WebConfig implements WebMvcConfigurer {

    private final OAuth2ProviderConverter oauth2ProviderConverter;

    @Override
    public void addFormatters(org.springframework.format.FormatterRegistry registry) {
        registry.addConverter(oauth2ProviderConverter);
    }
}
