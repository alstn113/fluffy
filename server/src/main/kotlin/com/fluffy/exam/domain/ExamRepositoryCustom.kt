package com.fluffy.exam.domain

import com.fluffy.exam.domain.dto.ExamDetailSummaryDto
import com.fluffy.exam.domain.dto.ExamSummaryDto
import com.fluffy.exam.domain.dto.MyExamSummaryDto
import com.fluffy.exam.domain.dto.SubmittedExamSummaryDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface ExamRepositoryCustom {

    fun findPublishedExamSummaries(pageable: Pageable): Page<ExamSummaryDto>

    fun findMyExamSummaries(pageable: Pageable, status: ExamStatus, memberId: Long): Page<MyExamSummaryDto>

    fun findSubmittedExamSummaries(pageable: Pageable, memberId: Long): Page<SubmittedExamSummaryDto>

    fun findExamDetailSummary(examId: Long): ExamDetailSummaryDto?
}