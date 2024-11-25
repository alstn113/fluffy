package com.pass.exam.application.dto.question;

public record TrueOrFalseQuestionAppRequest(
        String text,
        String type,
        boolean trueOrFalse
) implements QuestionAppRequest {
}
