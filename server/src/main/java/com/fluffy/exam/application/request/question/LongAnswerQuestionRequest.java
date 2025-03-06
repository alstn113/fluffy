package com.fluffy.exam.application.request.question;

public record LongAnswerQuestionRequest(
        String text,
        String passage,
        String type
) implements QuestionRequest {
}
