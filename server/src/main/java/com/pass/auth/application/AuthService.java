package com.pass.auth.application;

import com.pass.auth.application.dto.TokenResponse;
import com.pass.auth.domain.Member;
import com.pass.auth.domain.MemberRepository;
import com.pass.auth.domain.OAuth2Provider;
import com.pass.oauth2.domain.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    public Long getMemberIdByToken(String token) {
        return tokenProvider.getMemberId(token);
    }

    @Transactional
    public TokenResponse createToken(OAuth2UserInfo userInfo, OAuth2Provider provider) {
        Member member = findOrCreateMember(userInfo, provider);
        String accessToken = tokenProvider.createToken(member.getId().toString());

        return new TokenResponse(accessToken);
    }

    private Member findOrCreateMember(OAuth2UserInfo userInfo, OAuth2Provider provider) {
        return memberRepository.findBySocialIdAndProvider(userInfo.socialId(), provider)
                .orElseGet(() -> memberRepository.save(userInfo.toMember(provider)));
    }
}
