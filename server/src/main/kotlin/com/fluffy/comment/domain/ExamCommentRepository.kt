package com.fluffy.comment.domain

import com.fluffy.global.exception.NotFoundException
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.findByIdOrNull

interface ExamCommentRepository : JpaRepository<ExamComment, Long>, ExamCommentRepositoryCustom

fun ExamCommentRepository.findByIdOrThrow(id: Long): ExamComment {
    return findByIdOrNull(id)
        ?: throw NotFoundException("존재하지 않는 댓글입니다. 댓글 식별자: $id")
}