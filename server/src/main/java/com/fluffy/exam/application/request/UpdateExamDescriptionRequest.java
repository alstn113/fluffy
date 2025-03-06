package com.fluffy.exam.application.request;

import com.fluffy.global.web.Accessor;

public record UpdateExamDescriptionRequest(
        String description,
        Long examId,
        Accessor accessor
) {
}
