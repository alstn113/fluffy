package com.pass.form.query.dto.question;

public record AnswerQuestionResponse(
        Long id,
        String text,
        int sequence,
        String type
) implements QuestionResponse {
}
