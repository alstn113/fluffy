package com.pass.exam.command.application.dto.question;

public record TrueOrFalseQuestionAppRequest(
        String text,
        String type,
        boolean trueOrFalse
) implements QuestionAppRequest {
}
