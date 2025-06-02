package com.fluffy.submission.application

import com.fluffy.exam.domain.Exam
import com.fluffy.global.distributedLock.DistributedLock
import com.fluffy.global.exception.BadRequestException
import com.fluffy.submission.application.request.SubmissionRequest
import com.fluffy.submission.domain.SubmissionRepository
import org.springframework.stereotype.Service

@Service
class SubmissionLockService(
    private val submissionRepository: SubmissionRepository,
) {

    @DistributedLock(key = "#lockName")
    fun submitWithLock(request: SubmissionRequest, exam: Exam, lockName: String) {
        if (submissionRepository.existsByExamIdAndMemberId(exam.id, request.accessor.id)) {
            throw BadRequestException("한 번만 제출 가능합니다.")
        }

        val submission = SubmissionAssembler.toSubmission(exam, request.accessor.id, request)
        submissionRepository.save(submission)
    }
}