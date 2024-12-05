package com.pass.exam.application.dto;

import com.pass.global.web.Accessor;

public record UpdateExamInfoAppRequest(
        String title,
        String description,
        Long examId,
        Accessor accessor
) {
}
