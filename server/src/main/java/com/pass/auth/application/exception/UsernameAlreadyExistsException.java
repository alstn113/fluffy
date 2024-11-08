package com.pass.auth.application.exception;

import com.pass.global.exception.BadRequestException;

public class UsernameAlreadyExistsException extends BadRequestException {

    private static final String MESSAGE = "이미 존재하는 사용자 이름입니다.";

    public UsernameAlreadyExistsException() {
        super(MESSAGE);
    }

    public UsernameAlreadyExistsException(Throwable cause) {
        super(MESSAGE, cause);
    }
}
