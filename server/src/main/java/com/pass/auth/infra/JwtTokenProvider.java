package com.pass.auth.infra;

import org.springframework.stereotype.Component;
import com.pass.auth.application.TokenProvider;

@Component
public class JwtTokenProvider implements TokenProvider {

    @Override
    public String createToken(String memberId) {
        return "";
    }

    @Override
    public Long getMemberId(String token) {
        return 1L;
    }
}
