package com.fluffy.oauth2.ui

import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class OAuth2WebConfig(
    private val oauth2ProviderConverter: OAuth2ProviderConverter,
) : WebMvcConfigurer {

    override fun addFormatters(registry: FormatterRegistry) {
        registry.addConverter(oauth2ProviderConverter)
    }
}