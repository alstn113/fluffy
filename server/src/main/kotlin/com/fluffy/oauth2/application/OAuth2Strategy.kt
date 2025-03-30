package com.fluffy.oauth2.application

import com.fluffy.auth.domain.OAuth2Provider
import com.fluffy.oauth2.domain.OAuth2UserInfo

interface OAuth2Strategy {

    fun buildOAuth2LoginUrl(next: String): String

    fun fetchOAuth2UserInfo(code: String): OAuth2UserInfo

    fun buildClientRedirectUrl(next: String): String

    fun getProvider(): OAuth2Provider
}