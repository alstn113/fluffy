package com.fluffy.oauth2.domain;

import com.fluffy.auth.domain.Member;
import com.fluffy.auth.domain.OAuth2Provider;
import jakarta.annotation.Nullable;

public record OAuth2UserInfo(
        String socialId,
        String name,
        @Nullable String email,
        String avatarUrl
) {

    public Member toMember(OAuth2Provider provider) {
        return new Member(
                email,
                provider,
                socialId,
                name,
                avatarUrl
        );
    }
}
