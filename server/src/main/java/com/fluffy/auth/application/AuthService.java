package com.fluffy.auth.application;

import com.fluffy.auth.application.response.MyInfoResponse;
import com.fluffy.auth.application.response.TokenResponse;
import com.fluffy.auth.domain.Member;
import com.fluffy.auth.domain.MemberRepository;
import com.fluffy.auth.domain.OAuth2Provider;
import com.fluffy.oauth2.domain.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    public Long getMemberIdByToken(String token) {
        return tokenProvider.getMemberId(token);
    }

    @Transactional(readOnly = true)
    public MyInfoResponse getMyInfo(Long memberId) {
        Member member = memberRepository.findByIdOrThrow(memberId);

        return memberMapper.toMyInfoResponse(member);
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
