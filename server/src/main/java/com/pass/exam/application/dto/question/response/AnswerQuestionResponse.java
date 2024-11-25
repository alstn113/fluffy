package com.pass.exam.application.dto.question.response;

public record AnswerQuestionResponse(
        Long id,
        String text,
        String type
) implements QuestionResponse {
}
