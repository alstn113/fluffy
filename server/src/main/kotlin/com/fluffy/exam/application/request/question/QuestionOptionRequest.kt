package com.fluffy.exam.application.request.question

data class QuestionOptionRequest(
    val text: String,
    val isCorrect: Boolean
)
