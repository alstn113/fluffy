package com.pass.exam.query.dto;

import com.pass.exam.query.dto.question.QuestionResponse;
import java.time.LocalDateTime;
import java.util.List;

public record ExamDataResponse(
        String id,
        String title,
        String description,
        List<QuestionResponse> questions,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
