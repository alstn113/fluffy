package com.pass.auth.infra.exception;

import com.pass.global.exception.UnauthorizedException;

public class TokenExpiredException extends UnauthorizedException {

    private static final String MESSAGE = "토큰이 만료되었습니다.";

    public TokenExpiredException(Throwable cause) {
        super(MESSAGE, cause);
    }
}
