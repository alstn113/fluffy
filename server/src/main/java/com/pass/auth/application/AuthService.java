package com.pass.auth.application;

import com.pass.auth.application.exception.PasswordIncorrectException;
import com.pass.auth.application.exception.UsernameAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.pass.auth.application.dto.LoginAppRequest;
import com.pass.auth.application.dto.SignupAppRequest;
import com.pass.auth.application.dto.TokenResponse;
import com.pass.auth.domain.Member;
import com.pass.auth.domain.MemberRepository;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;

    public void signup(SignupAppRequest request) {
        validateUsernameAlreadyExists(request.username());

        String encodedPassword = passwordEncoder.encode(request.password());
        Member member = new Member(request.username(), encodedPassword);

        memberRepository.save(member);
    }

    private void validateUsernameAlreadyExists(String username) {
        if (memberRepository.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException();
        }
    }

    public TokenResponse login(LoginAppRequest request) {
        Member member = memberRepository.getByUsername(request.username());
        validatePasswordMatches(request, member);
        String token = tokenProvider.createToken(member.getId().toString());

        return new TokenResponse(token);
    }

    private void validatePasswordMatches(LoginAppRequest request, Member member) {
        if (!passwordEncoder.matches(request.password(), member.getPassword())) {
            throw new PasswordIncorrectException();
        }
    }

    public Long getMemberIdByToken(String token) {
        return tokenProvider.getMemberId(token);
    }
}
