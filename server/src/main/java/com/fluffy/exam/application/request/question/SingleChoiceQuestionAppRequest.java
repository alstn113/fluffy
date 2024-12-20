package com.fluffy.exam.application.request.question;

import java.util.List;

public record SingleChoiceQuestionAppRequest(
        String text,
        String type,
        List<QuestionOptionRequest> options
) implements QuestionAppRequest {
}
