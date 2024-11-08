package com.pass.form.query.dto;

import com.pass.form.query.dto.question.QuestionResponse;
import java.time.LocalDateTime;
import java.util.List;

public record FormResponse(
        String id,
        String title,
        String description,
        List<QuestionResponse> questions,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
