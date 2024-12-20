package com.fluffy.exam.application.request.question;

public record LongAnswerQuestionAppRequest(
        String text,
        String type
) implements QuestionAppRequest {
}
