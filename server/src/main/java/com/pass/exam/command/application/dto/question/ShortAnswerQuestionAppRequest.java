package com.pass.exam.command.application.dto.question;


public record ShortAnswerQuestionAppRequest(
        String text,
        String type,
        String correctAnswer
) implements QuestionAppRequest {
}
