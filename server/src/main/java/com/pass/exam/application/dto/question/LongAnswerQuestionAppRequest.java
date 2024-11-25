package com.pass.exam.application.dto.question;

public record LongAnswerQuestionAppRequest(
        String text,
        String type
) implements QuestionAppRequest {
}
