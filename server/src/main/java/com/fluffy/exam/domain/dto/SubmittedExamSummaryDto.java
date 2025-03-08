package com.fluffy.exam.domain.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;

public record SubmittedExamSummaryDto(
        Long examId,
        String title,
        String description,
        AuthorDto author,
        Long submissionCount,
        LocalDateTime lastSubmissionDate
) {

    @QueryProjection
    public SubmittedExamSummaryDto {
    }
}
