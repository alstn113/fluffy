package com.pass.submission.application.dto;

import com.pass.submission.domain.dto.ParticipantDto;
import java.time.LocalDateTime;
import java.util.List;

public record SubmissionDetailResponse(
        Long id,
        List<AnswerBaseResponse> answers,
        ParticipantDto participant,
        LocalDateTime submittedAt
) {

    public interface AnswerBaseResponse {
        Long id();

        Long questionId();

        String text();

        String type();
    }

    public record TextAnswerResponse(
            Long id,
            Long questionId,
            String text,
            String type,
            String answer,
            String correctAnswer
    ) implements AnswerBaseResponse {
    }

    public record ChoiceAnswerResponse(
            Long id,
            Long questionId,
            String text,
            String type,
            List<ChoiceResponse> choices
    ) implements AnswerBaseResponse {
    }

    public record ChoiceResponse(
            Long questionOptionId,
            String text,
            boolean isCorrect,
            boolean isSelected
    ) {
    }
}
