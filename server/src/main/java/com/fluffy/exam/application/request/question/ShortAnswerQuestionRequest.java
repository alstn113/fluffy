package com.fluffy.exam.application.request.question;


public record ShortAnswerQuestionRequest(
        String text,
        String passage,
        String type,
        String correctAnswer
) implements QuestionRequest {
}
