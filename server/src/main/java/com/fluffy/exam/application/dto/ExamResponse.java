package com.fluffy.exam.application.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ExamResponse(
        Long id,
        String title,
        String description,
        String status,
        List<QuestionResponse> questions,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public interface QuestionResponse {
        Long id();

        String text();

        String type();
    }

    public record AnswerQuestionResponse(
            Long id,
            String text,
            String type
    ) implements QuestionResponse {
    }

    public record ChoiceQuestionResponse(
            Long id,
            String text,
            String type,
            List<QuestionOptionResponse> options
    ) implements QuestionResponse {

        public record QuestionOptionResponse(
                Long id,
                String text
        ) {
        }
    }
}
