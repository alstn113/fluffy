package com.fluffy.exam.application.dto;

import com.fluffy.global.web.Accessor;

public record UpdateExamDescriptionAppRequest(
        String description,
        Long examId,
        Accessor accessor
) {
}
