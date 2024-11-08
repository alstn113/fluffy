package com.pass.global.exception;

public abstract class ForbiddenException extends BaseException {

    protected ForbiddenException(String message) {
        super(message);
    }

    protected ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }
}
