package com.fluffy.comment.application

import com.fluffy.comment.domain.ExamCommentRepository
import com.fluffy.comment.domain.dto.ExamReplyCommentDto
import com.fluffy.comment.domain.dto.ExamRootCommentDto
import com.fluffy.comment.domain.findByIdOrThrow
import com.fluffy.exam.domain.ExamRepository
import com.fluffy.exam.domain.findByIdOrThrow
import com.fluffy.global.exception.NotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ExamCommentQueryService(
    private val examRepository: ExamRepository,
    private val examCommentRepository: ExamCommentRepository
) {

    @Transactional(readOnly = true)
    fun getRootComments(examId: Long): List<ExamRootCommentDto> {
        val exam = examRepository.findByIdOrThrow(examId)
        val rootComments = examCommentRepository.findRootComments(exam.id)

        return maskIfDeleted(rootComments)
    }

    private fun maskIfDeleted(rootComments: List<ExamRootCommentDto>): List<ExamRootCommentDto> {
        return rootComments.map { comment ->
            if (comment.isDeleted) comment.asDeleted() else comment
        }
    }

    @Transactional(readOnly = true)
    fun getReplyComments(commentId: Long): List<ExamReplyCommentDto> {
        val rootComment = examCommentRepository.findByIdOrThrow(commentId)
        val replies = examCommentRepository.findReplyComments(commentId)

        if (rootComment.isDeleted() && replies.isEmpty()) {
            throw NotFoundException("존재하지 않는 댓글입니다. 댓글 식별자: $commentId")
        }

        return replies
    }
}
