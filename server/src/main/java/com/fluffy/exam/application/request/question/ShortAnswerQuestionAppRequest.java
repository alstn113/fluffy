package com.fluffy.exam.application.request.question;


public record ShortAnswerQuestionAppRequest(
        String text,
        String type,
        String correctAnswer
) implements QuestionAppRequest {
}
