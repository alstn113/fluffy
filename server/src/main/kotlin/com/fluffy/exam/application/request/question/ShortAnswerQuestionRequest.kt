package com.fluffy.exam.application.request.question

data class ShortAnswerQuestionRequest(
    override val text: String,
    override val passage: String,
    override val type: String,
    val correctAnswer: String
) : QuestionRequest


