package com.fluffy.exam.ui.request

import com.fluffy.exam.application.request.PublishExamRequest
import com.fluffy.exam.application.request.question.QuestionRequest
import com.fluffy.global.web.Accessor

data class PublishExamWebRequest(val questions: List<QuestionRequest>) {

    fun toAppRequest(examId: Long, accessor: Accessor): PublishExamRequest {
        return PublishExamRequest(
            examId = examId,
            questions = questions,
            accessor = accessor
        )
    }
}
