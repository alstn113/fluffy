package com.pass.exam.command.domain.exception;

import com.pass.global.exception.NotFoundException;

public class ExamStatusNotFoundException extends NotFoundException {

    private static final String MESSAGE = "존재하지 않는 시험 상태입니다. (%s)";

    public ExamStatusNotFoundException(String status) {
        super(String.format(MESSAGE, status));
    }
}
