package com.fluffy.exam.application.request;

import com.fluffy.global.web.Accessor;

public record UpdateExamTitleAppRequest(
        String title,
        Long examId,
        Accessor accessor
) {
}
