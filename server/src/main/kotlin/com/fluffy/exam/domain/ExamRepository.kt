package com.fluffy.exam.domain

import com.fluffy.global.exception.NotFoundException
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull

interface ExamRepository : JpaRepository<Exam, Long>, ExamRepositoryCustom

fun ExamRepository.findByIdOrThrow(id: Long): Exam {
    return findByIdOrNull(id)
        ?: throw NotFoundException("존재하지 않는 시험입니다. 시험 식별자: $id")
}