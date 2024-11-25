package com.pass.exam.application.dto.question;

import java.util.List;

public record MultipleChoiceAppRequest(
        String text,
        String type,
        List<QuestionOptionRequest> options
) implements QuestionAppRequest {
}
