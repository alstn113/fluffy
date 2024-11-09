package com.pass.form.command.domain.exception;

import com.pass.global.exception.BadRequestException;

public class InvalidQuestionOptionLengthException extends BadRequestException {

    private static final String MESSAGE = "질문 옵션의 길이는 1~%d자 이어야 합니다.";

    public InvalidQuestionOptionLengthException(int maxLength) {
        super(MESSAGE.formatted(maxLength));
    }
}
