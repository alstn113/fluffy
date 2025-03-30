package com.fluffy.submission.application.response

interface AnswerBaseResponse {
    val id: Long
    val questionId: Long
    val text: String
    val type: String
}