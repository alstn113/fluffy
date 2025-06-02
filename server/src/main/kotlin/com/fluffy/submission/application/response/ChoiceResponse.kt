package com.fluffy.submission.application.response

data class ChoiceResponse(
    val questionOptionId: Long,
    val text: String,
    val isCorrect: Boolean,
    val isSelected: Boolean,
)