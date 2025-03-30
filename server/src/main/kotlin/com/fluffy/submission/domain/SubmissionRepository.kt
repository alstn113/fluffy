package com.fluffy.submission.domain

import com.fluffy.global.exception.NotFoundException
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull

interface SubmissionRepository : JpaRepository<Submission, Long>, SubmissionRepositoryCustom {

    fun existsByExamIdAndMemberId(examId: Long, memberId: Long): Boolean
}

fun SubmissionRepository.findByIdOrThrow(id: Long): Submission {
    return findByIdOrNull(id)
        ?: throw NotFoundException("존재하지 않는 제출입니다. 제출 식별자: $id")
}