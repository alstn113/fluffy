package com.pass.form.command.domain.excetion;

import com.pass.global.exception.BadRequestException;

public class InvalidQuestionSizeException extends BadRequestException {

    private static final String MESSAGE = "질문은 1~%d개만 허용됩니다.";

    public InvalidQuestionSizeException(int maxLength) {
        super(MESSAGE.formatted(maxLength));
    }

    public InvalidQuestionSizeException(int maxLength, Throwable cause) {
        super(MESSAGE.formatted(maxLength), cause);
    }
}
