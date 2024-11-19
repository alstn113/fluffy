package com.pass.submission.command.application.exception;

import com.pass.global.exception.BadRequestException;

public class InvalidAnswerException extends BadRequestException {

    private static final String MESSAGE = "유효하지 않은 제출 응답입니다.";

    public InvalidAnswerException() {
        super(MESSAGE);
    }
}
