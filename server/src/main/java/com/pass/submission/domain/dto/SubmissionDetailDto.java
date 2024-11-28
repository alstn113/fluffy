package com.pass.submission.domain.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubmissionDetailDto {

    private Long id;
    private List<AnswerDto> answers;
    private ParticipantDto participant;
    private LocalDateTime submittedAt;

    @QueryProjection
    public SubmissionDetailDto(
            Long id,
            List<AnswerDto> answers,
            ParticipantDto participant,
            LocalDateTime submittedAt
    ) {
        this.id = id;
        this.answers = answers;
        this.participant = participant;
        this.submittedAt = submittedAt;
    }
}