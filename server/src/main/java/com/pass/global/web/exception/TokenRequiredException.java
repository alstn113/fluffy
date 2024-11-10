package com.pass.global.web.exception;

import com.pass.global.exception.UnauthorizedException;

public class TokenRequiredException extends UnauthorizedException {

    private static final String MESSAGE = "권한을 위한 토큰이 필요합니다.";

    public TokenRequiredException() {
        super(MESSAGE);
    }
}