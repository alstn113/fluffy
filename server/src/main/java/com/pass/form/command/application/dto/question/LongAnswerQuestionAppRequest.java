package com.pass.form.command.application.dto.question;

public record LongAnswerQuestionAppRequest(
        String text,
        String type
) implements QuestionAppRequest {
}
