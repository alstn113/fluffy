package com.pass.exam.application.dto;

import com.pass.exam.application.dto.question.response.QuestionResponse;
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
