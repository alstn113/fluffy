package com.pass.exam.application.dto.question.request;


public record ShortAnswerQuestionAppRequest(
        String text,
        String type,
        String correctAnswer
) implements QuestionAppRequest {
}
