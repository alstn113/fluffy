package com.fluffy.submission.ui.request

import com.fluffy.global.web.Accessor
import com.fluffy.submission.application.request.QuestionResponseRequest
import com.fluffy.submission.application.request.SubmissionRequest

data class SubmissionWebRequest(
    val questionResponses: List<QuestionResponseRequest>
) {

    fun toAppRequest(examId: Long, accessor: Accessor): SubmissionRequest {
        return SubmissionRequest(
            examId = examId,
            questionResponses = questionResponses,
            accessor = accessor
        )
    }
}