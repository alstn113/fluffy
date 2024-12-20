package com.fluffy.submission.application.request;

import java.util.List;

public record QuestionResponseAppRequest(
        List<String> answers
) {
}
