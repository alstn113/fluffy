package com.fluffy.exam.application.request

import com.fluffy.exam.application.request.question.QuestionRequest
import com.fluffy.global.web.Accessor

data class UpdateExamQuestionsRequest(
    val examId: Long,
    val questions: List<QuestionRequest>,
    val accessor: Accessor,
)
