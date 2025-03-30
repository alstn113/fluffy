package com.fluffy.submission.domain.dto

import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDateTime

data class MySubmissionSummaryDto @QueryProjection constructor(
    val submissionId: Long,
    val submittedAt: LocalDateTime
)