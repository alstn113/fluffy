package com.fluffy.submission.application

import com.fluffy.auth.domain.MemberRepository
import com.fluffy.auth.domain.findByIdOrThrow
import com.fluffy.exam.domain.ExamRepository
import com.fluffy.exam.domain.findByIdOrThrow
import com.fluffy.global.exception.ForbiddenException
import com.fluffy.global.web.Accessor
import com.fluffy.submission.application.response.SubmissionDetailResponse
import com.fluffy.submission.domain.SubmissionRepository
import com.fluffy.submission.domain.dto.MySubmissionSummaryDto
import com.fluffy.submission.domain.dto.SubmissionSummaryDto
import com.fluffy.submission.domain.findByIdOrThrow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SubmissionQueryService(
    private val examRepository: ExamRepository,
    private val submissionRepository: SubmissionRepository,
    private val memberRepository: MemberRepository,
) {

    @Transactional(readOnly = true)
    fun getSummariesByExamId(examId: Long, accessor: Accessor): List<SubmissionSummaryDto> {
        val exam = examRepository.findByIdOrThrow(examId)

        if (exam.isNotWrittenBy(accessor.id)) {
            throw ForbiddenException("해당 시험 제출 목록을 조회할 권한이 없습니다.")
        }

        return submissionRepository.findSubmissionSummariesByExamId(examId)
    }

    @Transactional(readOnly = true)
    fun getDetail(examId: Long, submissionId: Long, accessor: Accessor): SubmissionDetailResponse {
        val exam = examRepository.findByIdOrThrow(examId)
        val submission = submissionRepository.findByIdOrThrow(submissionId)

        if (exam.isNotWrittenBy(accessor.id) && submission.isNotWrittenBy(accessor.id)) {
            throw ForbiddenException("해당 시험 제출을 조회할 권한이 없습니다.")
        }

        val submitter = memberRepository.findByIdOrThrow(accessor.id)

        return SubmissionAssembler.toDetailResponse(exam, submission, submitter)
    }

    @Transactional(readOnly = true)
    fun getMySubmissionSummaries(examId: Long, accessor: Accessor): List<MySubmissionSummaryDto> {
        return submissionRepository.findMySubmissionSummaries(examId, accessor.id)
    }
}
