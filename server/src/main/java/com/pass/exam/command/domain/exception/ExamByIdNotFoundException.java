package com.pass.exam.command.domain.exception;

import com.pass.global.exception.NotFoundException;

public class ExamByIdNotFoundException extends NotFoundException {

    private static final String MESSAGE = "존재하지 않는 시험입니다. 시험 식별자: %s";

    public ExamByIdNotFoundException(String examId) {
        super(String.format(MESSAGE, examId));
    }
}
