package com.fluffy.exam.application.request.question;

import java.util.List;

public record MultipleChoiceAppRequest(
        String text,
        String type,
        List<QuestionOptionRequest> options
) implements QuestionAppRequest {
}
