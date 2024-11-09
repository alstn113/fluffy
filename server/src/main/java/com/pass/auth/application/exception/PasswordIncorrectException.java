package com.pass.auth.application.exception;

import com.pass.global.exception.BadRequestException;

public class PasswordIncorrectException extends BadRequestException {

    private static final String MESSAGE = "옳바르지 않은 비밀번호입니다.";

    public PasswordIncorrectException() {
        super(MESSAGE);
    }
}
