package com.fluffy.exam.application.request.question;

import java.util.List;

public record SingleChoiceQuestionRequest(
        String text,
        String passage,
        String type,
        List<QuestionOptionRequest> options
) implements QuestionRequest {
}
