package com.fluffy.exam.application.request.question;

public record TrueOrFalseQuestionAppRequest(
        String text,
        String type,
        boolean trueOrFalse
) implements QuestionAppRequest {
}
