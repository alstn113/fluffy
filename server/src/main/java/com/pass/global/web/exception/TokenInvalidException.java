package com.pass.global.web.exception;

import com.pass.global.exception.UnauthorizedException;

public class TokenInvalidException extends UnauthorizedException {

    private static final String MESSAGE = "토큰이 유효하지 않습니다.";

    public TokenInvalidException() {
        super(MESSAGE);
    }
}
