package com.pass.submission.command.domain.exception;

import com.pass.global.exception.BadRequestException;

public class ExamIdMismatchException extends BadRequestException {

    private static final String MESSAGE = "시험 식별자가 일치하지 않습니다. (%s)";

    public ExamIdMismatchException(String examId) {
        super(MESSAGE.formatted(examId));
    }
}
