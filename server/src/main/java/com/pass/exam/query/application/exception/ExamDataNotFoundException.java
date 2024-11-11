package com.pass.exam.query.application.exception;

import com.pass.global.exception.NotFoundException;

public class ExamDataNotFoundException extends NotFoundException {

    private static final String MESSAGE = "존재하지 않는 시험입니다. examId: %s";

    public ExamDataNotFoundException(String examId) {
        super(String.format(MESSAGE, examId));
    }
}
