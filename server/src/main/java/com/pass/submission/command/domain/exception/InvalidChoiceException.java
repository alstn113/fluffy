package com.pass.submission.command.domain.exception;

import com.pass.global.exception.BadRequestException;

public class InvalidChoiceException extends BadRequestException {

    private static final String MESSAGE = "유효하지 않은 제출 응답 선택지입니다.";

    public InvalidChoiceException() {
        super(MESSAGE);
    }
}
