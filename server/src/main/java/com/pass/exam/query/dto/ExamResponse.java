package com.pass.exam.query.dto;

import com.pass.exam.query.dto.question.QuestionResponse;
import java.time.LocalDateTime;
import java.util.List;

public record ExamResponse(
        Long id,
        String title,
        String description,
        List<QuestionResponse> questions,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
