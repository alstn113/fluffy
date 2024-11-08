package com.pass.form.command.application.dto.question;

import java.util.List;

public record SingleChoiceQuestionAppRequest(
        String text,
        String type,
        List<String> options,
        int correctOptionNumber
) implements QuestionAppRequest {
}
