package com.fluffy.submission.application

import com.fluffy.exam.domain.ExamRepository
import com.fluffy.exam.domain.findByIdOrThrow
import com.fluffy.global.exception.BadRequestException
import com.fluffy.submission.application.request.SubmissionRequest
import com.fluffy.submission.domain.Submission
import com.fluffy.submission.domain.SubmissionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SubmissionService(
    private val examRepository: ExamRepository,
    private val submissionRepository: SubmissionRepository,
    private val submissionLockService: SubmissionLockService,
) {

    @Transactional
    fun submit(request: SubmissionRequest) {
        val memberId = request.accessor.id
        val exam = examRepository.findByIdOrThrow(request.examId)

        if (exam.isNotPublished()) {
            throw BadRequestException("시험이 공개되지 않았습니다.")
        }

        if (exam.isSingleAttempt) {
            val lockName = "submit:${exam.id}:${memberId}"
            submissionLockService.submitWithLock(request, exam, lockName)
            return
        }

        val submission: Submission = SubmissionAssembler.toSubmission(exam, memberId, request)
        submissionRepository.save(submission)
    }
}