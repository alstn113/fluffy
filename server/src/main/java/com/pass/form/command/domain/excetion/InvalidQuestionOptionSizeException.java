package com.pass.form.command.domain.excetion;

import com.pass.global.exception.BadRequestException;

public class InvalidQuestionOptionSizeException extends BadRequestException  {

    private static final String MESSAGE = "질문 옵션은 1~%d개만 허용됩니다.";

    public InvalidQuestionOptionSizeException(int maxLength) {
        super(MESSAGE.formatted(maxLength));
    }

    public InvalidQuestionOptionSizeException(int maxLength, Throwable cause) {
        super(MESSAGE.formatted(maxLength), cause);
    }
}
