package com.fluffy.submission.domain.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;

public record MySubmissionSummaryDto(
        Long submissionId,
        LocalDateTime submittedAt
) {

    @QueryProjection
    public MySubmissionSummaryDto {
    }
}
