package com.pass.auth.application;

public interface TokenProvider {

    String createToken(String memberId);

    Long getMemberId(String token);
}
