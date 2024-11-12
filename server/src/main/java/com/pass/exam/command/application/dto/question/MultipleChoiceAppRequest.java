package com.pass.exam.command.application.dto.question;

import java.util.List;

public record MultipleChoiceAppRequest(
        String text,
        String type,
        List<String> options,
        List<Integer> correctOptionNumbers
) implements QuestionAppRequest {
}