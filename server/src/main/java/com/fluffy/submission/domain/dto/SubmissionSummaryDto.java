package com.fluffy.submission.domain.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;

public record SubmissionSummaryDto(
        Long id,
        ParticipantDto participant,
        LocalDateTime submittedAt
) {

    @QueryProjection
    public SubmissionSummaryDto {
    }
}