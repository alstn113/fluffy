package com.pass.exam.query.dto.question;

public record AnswerQuestionResponse(
        Long id,
        String text,
        String type
) implements QuestionResponse {
}
