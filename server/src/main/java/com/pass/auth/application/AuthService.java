package com.pass.auth.application;

import com.pass.auth.domain.MemberRepository;
import com.pass.auth.domain.OAuth2Provider;
import com.pass.oauth2.domain.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    public Long getMemberIdByToken(String token) {
        return tokenProvider.getMemberId(token);
    }

    public void findOrCreateMember(OAuth2UserInfo userInfo, OAuth2Provider provider) {
    }
}
