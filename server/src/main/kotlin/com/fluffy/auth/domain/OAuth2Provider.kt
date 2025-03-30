package com.fluffy.auth.domain

import com.fluffy.global.exception.NotFoundException

enum class OAuth2Provider {

    GITHUB,
    GOOGLE,
    ;

    companion object {

        fun from(provider: String): OAuth2Provider {
            return entries.find { it.name.equals(provider, ignoreCase = true) }
                ?: throw NotFoundException("존재하지 않는 OAuth2 제공자입니다. 제공자: $provider")
        }
    }
}