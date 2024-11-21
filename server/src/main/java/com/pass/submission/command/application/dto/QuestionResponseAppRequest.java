package com.pass.submission.command.application.dto;

import java.util.List;

public record QuestionResponseAppRequest(
        List<String> answers
) {
}
