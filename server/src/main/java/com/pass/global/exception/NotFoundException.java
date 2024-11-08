package com.pass.global.exception;

public abstract class NotFoundException extends BaseException {

    protected NotFoundException(String message) {
        super(message);
    }

    protected NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
