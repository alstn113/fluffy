package com.fluffy.exam.application.response

import java.time.LocalDateTime

data class ExamDetailResponse(
    val id: Long,
    val title: String,
    val description: String,
    val status: String,
    val questions: List<QuestionResponse>,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)

interface QuestionResponse {

    val id: Long
    val text: String
    val passage: String
    val type: String
}

data class AnswerQuestionResponse(
    override val id: Long,
    override val text: String,
    override val passage: String,
    override val type: String,
) : QuestionResponse

data class ChoiceQuestionResponse(
    override val id: Long,
    override val text: String,
    override val passage: String,
    override val type: String,
    val options: List<QuestionOptionResponse>,
) : QuestionResponse


data class QuestionOptionResponse(
    val id: Long,
    val text: String,
)
