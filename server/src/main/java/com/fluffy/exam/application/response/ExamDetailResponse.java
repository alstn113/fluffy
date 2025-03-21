package com.fluffy.exam.application.response;

import java.time.LocalDateTime;
import java.util.List;

public record ExamDetailResponse(
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

        String passage();

        String type();
    }

    public record AnswerQuestionResponse(
            Long id,
            String text,
            String passage,
            String type
    ) implements QuestionResponse {
    }

    public record ChoiceQuestionResponse(
            Long id,
            String text,
            String passage,
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
