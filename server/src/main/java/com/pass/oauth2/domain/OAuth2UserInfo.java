package com.pass.oauth2.domain;

import com.pass.auth.domain.Member;
import com.pass.auth.domain.OAuth2Provider;
import jakarta.annotation.Nullable;

public record OAuth2UserInfo(
        Long socialId,
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
