package com.fluffy.exam.application.dto;

import com.fluffy.global.web.Accessor;

public record UpdateExamTitleAppRequest(
        String title,
        Long examId,
        Accessor accessor
) {
}
