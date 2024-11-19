package com.pass.submission.command.domain.exception;

import com.pass.global.exception.BadRequestException;

public class QuestionSizeMismatchException extends BadRequestException {

    private static final String MESSAGE = "질문들에 대한 응답의 크기가 일치하지 않습니다. (질문 크기: %s, 응답 크기: %s)";

    public QuestionSizeMismatchException(int questionSize, int answerSize) {
        super(MESSAGE.formatted(questionSize, answerSize));
    }
}
