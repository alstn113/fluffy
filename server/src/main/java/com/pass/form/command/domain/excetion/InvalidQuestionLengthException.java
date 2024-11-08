package com.pass.form.command.domain.excetion;

import com.pass.global.exception.BadRequestException;

public class InvalidQuestionLengthException extends BadRequestException {

    private static final String MESSAGE = "질문의 길이는 1~%d자 이어야 합니다.";

    public InvalidQuestionLengthException(int maxLength) {
        super(MESSAGE.formatted(maxLength));
    }

    public InvalidQuestionLengthException(int maxLength, Throwable cause) {
        super(MESSAGE.formatted(maxLength), cause);
    }
}
