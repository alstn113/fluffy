package com.fluffy.submission.application.response

data class TextAnswerResponse(
    override val id: Long,
    override val questionId: Long,
    override val text: String,
    override val type: String,
    val answer: String,
    val correctAnswer: String?,
) : AnswerBaseResponse