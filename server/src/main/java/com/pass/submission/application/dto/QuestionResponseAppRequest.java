package com.pass.submission.application.dto;

import java.util.List;

public record QuestionResponseAppRequest(
        List<String> answers
) {
}
