package com.pass.form.command.domain.excetion;

import com.pass.global.exception.BadRequestException;

public class InvalidSingleChoiceCorrectAnswerSizeException extends BadRequestException {

    private static final String MESSAGE = "객관식 단일 선택은 정답이 1개여야 합니다.";

    public InvalidSingleChoiceCorrectAnswerSizeException() {
        super(MESSAGE);
    }

    public InvalidSingleChoiceCorrectAnswerSizeException(Throwable cause) {
        super(MESSAGE, cause);
    }
}