package com.fluffy.submission.domain.dto

import java.time.LocalDateTime

data class MySubmissionSummaryDto(
    val submissionId: Long,
    val submittedAt: LocalDateTime,
)