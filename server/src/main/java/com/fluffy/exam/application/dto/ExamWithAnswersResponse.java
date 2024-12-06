package com.fluffy.exam.application.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ExamWithAnswersResponse(
        Long id,
        String title,
        String description,
        String status,
        List<QuestionWithAnswersResponse> questions,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public interface QuestionWithAnswersResponse {
        Long id();

        String text();

        String type();
    }

    public record AnswerQuestionWithAnswersResponse(
            Long id,
            String text,
            String type,
            String correctAnswer
    ) implements QuestionWithAnswersResponse {
    }

    public record ChoiceQuestionWithAnswersResponse(
            Long id,
            String text,
            String type,
            List<QuestionOptionWithAnswersResponse> options
    ) implements QuestionWithAnswersResponse {

        public record QuestionOptionWithAnswersResponse(
                Long id,
                String text,
                Boolean isCorrect
        ) {
        }
    }
}
