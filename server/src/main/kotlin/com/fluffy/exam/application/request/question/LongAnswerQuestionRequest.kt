package com.fluffy.exam.application.request.question

data class LongAnswerQuestionRequest(
    override val text: String,
    override val passage: String,
    override val type: String,
) : QuestionRequest