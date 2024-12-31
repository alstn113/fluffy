package com.fluffy.submission.domain.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MySubmissionSummaryDto {

    private Long submissionId;
    private LocalDateTime submittedAt;

    @QueryProjection
    public MySubmissionSummaryDto(Long submissionId, LocalDateTime submittedAt) {
        this.submissionId = submissionId;
        this.submittedAt = submittedAt;
    }
}
