package com.fluffy.submission.domain

import com.fluffy.submission.domain.dto.MySubmissionSummaryDto
import com.fluffy.submission.domain.dto.SubmissionSummaryDto

interface SubmissionRepositoryCustom {

    fun findSubmissionSummariesByExamId(examId: Long): List<SubmissionSummaryDto>

    fun findMySubmissionSummaries(examId: Long, memberId: Long): List<MySubmissionSummaryDto>
}