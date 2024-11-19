package com.pass.submission.command.domain.exception;

import com.pass.global.exception.BadRequestException;

public class QuestionIdMismatchException extends BadRequestException {

    private static final String MESSAGE = "질문 식별자가 일치하지 않습니다. (예상: %d, 실제: %d)";

    public QuestionIdMismatchException(Long expected, Long actual) {
        super(String.format(MESSAGE, expected, actual));
    }
}
