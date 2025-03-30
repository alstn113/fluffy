package com.fluffy.exam.application.request

import com.fluffy.global.web.Accessor

data class UpdateExamTitleRequest(
    val title: String,
    val examId: Long,
    val accessor: Accessor
)