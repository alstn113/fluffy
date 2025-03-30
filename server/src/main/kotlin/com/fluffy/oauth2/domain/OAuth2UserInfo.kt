package com.fluffy.oauth2.domain

import com.fluffy.auth.domain.Member
import com.fluffy.auth.domain.OAuth2Provider


data class OAuth2UserInfo(
    val socialId: String,
    val name: String,
    val email: String?,
    val avatarUrl: String
) {

    fun toMember(provider: OAuth2Provider): Member {
        return Member(
            email = email,
            provider = provider,
            socialId = socialId,
            name = name,
            avatarUrl = avatarUrl
        )
    }
}
