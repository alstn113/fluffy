package com.fluffy.exam.domain.dto;

import com.fluffy.exam.domain.ExamStatus;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;

public record MyExamSummaryDto(
        Long id,
        String title,
        String description,
        ExamStatus status,
        AuthorDto author,
        Long questionCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    @QueryProjection
    public MyExamSummaryDto {
    }
}
