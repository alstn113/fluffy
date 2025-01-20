package com.fluffy.exam.application.response;

import com.fluffy.exam.domain.dto.AuthorDto;
import java.time.LocalDateTime;

public record ExamDetailSummaryResponse(
        Long id,
        String title,
        String description,
        String status,
        AuthorDto author,
        Long questionCount,
        Long likeCount,
        boolean isLiked,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
