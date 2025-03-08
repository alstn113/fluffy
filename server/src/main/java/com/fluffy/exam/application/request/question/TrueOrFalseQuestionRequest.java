package com.fluffy.exam.application.request.question;

public record TrueOrFalseQuestionRequest(
        String text,
        String passage,
        String type,
        boolean trueOrFalse
) implements QuestionRequest {
}
