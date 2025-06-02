package com.fluffy.submission.domain.dto

import java.time.LocalDateTime

data class SubmissionSummaryDto(
    val id: Long,
    val participant: ParticipantDto,
    val submittedAt: LocalDateTime,
)