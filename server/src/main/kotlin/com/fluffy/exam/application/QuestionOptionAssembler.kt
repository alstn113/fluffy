package com.fluffy.exam.application

import com.fluffy.exam.application.response.QuestionOptionResponse
import com.fluffy.exam.application.response.QuestionOptionWithAnswersResponse
import com.fluffy.exam.domain.QuestionOption

object QuestionOptionAssembler {

    fun toResponses(questionOptions: List<QuestionOption>): List<QuestionOptionResponse> {
        return questionOptions.map { toResponse(it) }
    }

    private fun toResponse(questionOption: QuestionOption): QuestionOptionResponse {
        return QuestionOptionResponse(
            questionOption.id,
            questionOption.text,
        )
    }

    fun toWithAnswersResponses(questionOptions: List<QuestionOption>): List<QuestionOptionWithAnswersResponse> {
        return questionOptions.map { toWithAnswersResponse(it) }
    }

    private fun toWithAnswersResponse(questionOption: QuestionOption): QuestionOptionWithAnswersResponse {
        return QuestionOptionWithAnswersResponse(
            questionOption.id,
            questionOption.text,
            questionOption.isCorrect,
        )
    }
}
