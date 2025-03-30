package com.fluffy.submission.application.request

import com.fluffy.global.web.Accessor

data class SubmissionRequest(
    val examId: Long,
    val questionResponses: List<QuestionResponseRequest>,
    val accessor: Accessor
)
