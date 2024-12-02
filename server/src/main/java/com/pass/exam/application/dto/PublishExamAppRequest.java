package com.pass.exam.application.dto;

import com.pass.global.web.Accessor;
import java.time.LocalDateTime;

public record PublishExamAppRequest(
        Long examId,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Accessor accessor
) {
}
