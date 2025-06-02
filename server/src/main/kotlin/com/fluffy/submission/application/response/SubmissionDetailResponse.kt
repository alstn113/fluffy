package com.fluffy.submission.application.response

import com.fluffy.submission.domain.dto.ParticipantDto
import java.time.LocalDateTime

data class SubmissionDetailResponse(
    val id: Long,
    val answers: List<AnswerBaseResponse>,
    val participant: ParticipantDto,
    val submittedAt: LocalDateTime,
)
