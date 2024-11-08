package com.pass.form.command.domain.excetion;

import com.pass.global.exception.BadRequestException;

public class InvalidCorrectAnswerLengthException extends BadRequestException {

    private static final String MESSAGE = "정답의 길이는 1~%d자 이어야 합니다.";

    public InvalidCorrectAnswerLengthException(int maxLength) {
        super(MESSAGE.formatted(maxLength));
    }

    public InvalidCorrectAnswerLengthException(int maxLength, Throwable cause) {
        super(MESSAGE.formatted(maxLength), cause);
    }
}
