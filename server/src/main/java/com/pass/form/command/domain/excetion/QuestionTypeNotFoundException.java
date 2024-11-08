package com.pass.form.command.domain.excetion;

import com.pass.global.exception.BadRequestException;

public class QuestionTypeNotFoundException extends BadRequestException {

    private static final String MESSAGE = "존재하지 않는 질문 유형입니다.";

    public QuestionTypeNotFoundException() {
        super(MESSAGE);
    }

    public QuestionTypeNotFoundException(Throwable cause) {
        super(MESSAGE, cause);
    }
}
