package com.pass.exam.command.application.dto.question;

import java.util.List;

public record SingleChoiceQuestionAppRequest(
        String text,
        String type,
        List<QuestionOptionRequest> options
) implements QuestionAppRequest {
}
