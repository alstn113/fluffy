package com.pass.exam.domain.dto;

import com.pass.exam.domain.ExamStatus;
import java.time.LocalDateTime;

public record ExamSummaryDto(
        Long id,
        String title,
        String description,
        ExamStatus status,
        AuthorDto author,
        Long questionCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
