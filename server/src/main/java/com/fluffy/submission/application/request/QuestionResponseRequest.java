package com.fluffy.submission.application.request;

import java.util.List;

public record QuestionResponseRequest(
        List<String> answers
) {
}
