package com.fluffy.exam.application.response;

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

        String passage();

        String type();
    }

    public record AnswerQuestionWithAnswersResponse(
            Long id,
            String text,
            String passage,
            String type,
            String correctAnswer
    ) implements QuestionWithAnswersResponse {
    }

    public record ChoiceQuestionWithAnswersResponse(
            Long id,
            String text,
            String passage,
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
