package com.fluffy.exam.application.request

import com.fluffy.global.web.Accessor

data class CreateExamRequest(
    val title: String,
    val accessor: Accessor,
)
