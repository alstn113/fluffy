package com.fluffy.exam.application.response

import java.time.LocalDateTime

data class ExamWithAnswersResponse(
    val id: Long,
    val title: String,
    val description: String,
    val status: String,
    val questions: List<QuestionWithAnswersResponse>,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)

interface QuestionWithAnswersResponse {

    val id: Long
    val text: String
    val passage: String
    val type: String
}

data class AnswerQuestionWithAnswersResponse(
    override val id: Long,
    override val text: String,
    override val passage: String,
    override val type: String,
    val correctAnswer: String?
) : QuestionWithAnswersResponse

data class ChoiceQuestionWithAnswersResponse(
    override val id: Long,
    override val text: String,
    override val passage: String,
    override val type: String,
    val options: List<QuestionOptionWithAnswersResponse>
) : QuestionWithAnswersResponse

data class QuestionOptionWithAnswersResponse(
    val id: Long,
    val text: String,
    val isCorrect: Boolean
)
