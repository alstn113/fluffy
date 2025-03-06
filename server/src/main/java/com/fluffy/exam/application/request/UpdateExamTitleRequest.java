package com.fluffy.exam.application.request;

import com.fluffy.global.web.Accessor;

public record UpdateExamTitleRequest(
        String title,
        Long examId,
        Accessor accessor
) {
}
