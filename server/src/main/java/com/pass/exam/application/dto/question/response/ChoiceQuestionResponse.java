package com.pass.exam.application.dto.question.response;

import java.util.List;

public record ChoiceQuestionResponse(
        Long id,
        String text,
        String type,
        List<QuestionOptionResponse> options
) implements QuestionResponse {
}
