package com.fluffy.exam.application.request.question

data class TrueOrFalseQuestionRequest(
    override val text: String,
    override val passage: String,
    override val type: String,
    val trueOrFalse: Boolean,
) : QuestionRequest