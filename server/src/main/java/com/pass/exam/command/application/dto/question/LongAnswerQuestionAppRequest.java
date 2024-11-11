package com.pass.exam.command.application.dto.question;

public record LongAnswerQuestionAppRequest(
        String text,
        String type
) implements QuestionAppRequest {
}
