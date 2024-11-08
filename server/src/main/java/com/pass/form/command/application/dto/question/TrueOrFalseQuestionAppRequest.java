package com.pass.form.command.application.dto.question;

public record TrueOrFalseQuestionAppRequest(
        String text,
        String type,
        String trueText,
        String falseText,
        boolean trueOrFalse
) implements QuestionAppRequest {
}
