package com.fluffy.exam.ui.request

import com.fluffy.exam.application.request.UpdateExamQuestionsRequest
import com.fluffy.exam.application.request.question.QuestionRequest
import com.fluffy.global.web.Accessor

data class UpdateExamQuestionsWebRequest(val questions: List<QuestionRequest>) {

    fun toAppRequest(examId: Long, accessor: Accessor): UpdateExamQuestionsRequest {
        return UpdateExamQuestionsRequest(
            examId = examId,
            questions = questions,
            accessor = accessor
        )
    }
}
