package com.fluffy.oauth2.ui

import com.fluffy.auth.domain.OAuth2Provider
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class OAuth2ProviderConverter : Converter<String, OAuth2Provider> {

    override fun convert(source: String): OAuth2Provider {
        return OAuth2Provider.from(source)
    }
}
