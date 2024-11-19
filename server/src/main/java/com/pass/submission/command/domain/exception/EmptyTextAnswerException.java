package com.pass.submission.command.domain.exception;

import com.pass.global.exception.BadRequestException;

public class EmptyTextAnswerException extends BadRequestException {

    private static final String MESSAGE = "제출 응답 텍스트가 비어있습니다.";

    public EmptyTextAnswerException() {
        super(MESSAGE);
    }
}
