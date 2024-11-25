package com.pass.exam.application.dto.question.request;

public record TrueOrFalseQuestionAppRequest(
        String text,
        String type,
        boolean trueOrFalse
) implements QuestionAppRequest {
}
