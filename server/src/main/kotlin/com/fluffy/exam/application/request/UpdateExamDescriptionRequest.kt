package com.fluffy.exam.application.request

import com.fluffy.global.web.Accessor

data class UpdateExamDescriptionRequest(
    val description: String,
    val examId: Long,
    val accessor: Accessor,
)
