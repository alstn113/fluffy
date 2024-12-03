package com.pass.submission.domain.dto;

import java.time.LocalDateTime;

public record SubmissionSummaryDto(
        Long id,
        ParticipantDto participant,
        LocalDateTime submittedAt
) {
}
