package com.fluffy.exam.domain.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubmittedExamSummaryDto {

    private Long examId;
    private String title;
    private String description;
    private AuthorDto author;
    private Long submissionCount;
    private LocalDateTime lastSubmissionDate;

    @QueryProjection
    public SubmittedExamSummaryDto(
            Long examId,
            String title,
            String description,
            AuthorDto author,
            Long submissionCount,
            LocalDateTime lastSubmissionDate
    ) {
        this.examId = examId;
        this.title = title;
        this.description = description;
        this.author = author;
        this.submissionCount = submissionCount;
        this.lastSubmissionDate = lastSubmissionDate;
    }
}
