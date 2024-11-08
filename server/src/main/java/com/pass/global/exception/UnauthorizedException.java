package com.pass.global.exception;

public abstract class UnauthorizedException extends BaseException {

    protected UnauthorizedException(String message) {
        super(message);
    }

    protected UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}
