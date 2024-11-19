package com.pass.submission.command.domain.exception;

import com.pass.global.exception.BadRequestException;

public class InvalidTextAnswerException extends BadRequestException {

    private static final String MESSAGE = "유효하지 않은 제출 응답 텍스트입니다.";

    public InvalidTextAnswerException() {
        super(MESSAGE);
    }
}
