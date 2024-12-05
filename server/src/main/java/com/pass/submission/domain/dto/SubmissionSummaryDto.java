package com.pass.submission.domain.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubmissionSummaryDto {

    private Long id;
    private ParticipantDto participant;
    private LocalDateTime submittedAt;

    @QueryProjection
    public SubmissionSummaryDto(Long id, ParticipantDto participant, LocalDateTime submittedAt) {
        this.id = id;
        this.participant = participant;
        this.submittedAt = submittedAt;
    }
}