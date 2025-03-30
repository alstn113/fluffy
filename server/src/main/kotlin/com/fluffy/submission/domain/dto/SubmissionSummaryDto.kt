package com.fluffy.submission.domain.dto

import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDateTime

data class SubmissionSummaryDto @QueryProjection constructor(
    val id: Long,
    val participant: ParticipantDto,
    val submittedAt: LocalDateTime,
)