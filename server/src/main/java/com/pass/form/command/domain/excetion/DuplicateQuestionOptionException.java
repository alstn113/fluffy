package com.pass.form.command.domain.excetion;

import com.pass.global.exception.BadRequestException;

public class DuplicateQuestionOptionException extends BadRequestException {

    private static final String MESSAGE = "중복된 질문 옵션은 허용되지 않습니다.";

    public DuplicateQuestionOptionException() {
        super(MESSAGE);
    }

    public DuplicateQuestionOptionException(Throwable cause) {
        super(MESSAGE, cause);
    }
}
