package com.fluffy.exam.domain.dto

import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDateTime

data class SubmittedExamSummaryDto @QueryProjection constructor(
    val examId: Long,
    val title: String,
    val description: String,
    val author: AuthorDto,
    val submissionCount: Long,
    val lastSubmissionDate: LocalDateTime
)
