package com.fluffy.exam.application.dto.question;


public record ShortAnswerQuestionAppRequest(
        String text,
        String type,
        String correctAnswer
) implements QuestionAppRequest {
}
