package com.pass.auth.infra.exception;

import com.pass.global.exception.UnauthorizedException;

public class InvalidTokenException extends UnauthorizedException {

    private static final String MESSAGE = "유효하지 않은 토큰입니다.";

    public InvalidTokenException() {
        super(MESSAGE);
    }

    public InvalidTokenException(Throwable cause) {
        super(MESSAGE, cause);
    }
}
