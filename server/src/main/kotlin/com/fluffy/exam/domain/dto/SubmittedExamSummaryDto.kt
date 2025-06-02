package com.fluffy.exam.domain.dto

import java.time.LocalDateTime

data class SubmittedExamSummaryDto(
    val examId: Long,
    val title: String,
    val description: String,
    val author: AuthorDto,
    val submissionCount: Long,
    val lastSubmissionDate: LocalDateTime,
)
