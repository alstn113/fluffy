package com.fluffy.comment.application

import com.fluffy.auth.domain.MemberRepository
import com.fluffy.auth.domain.findByIdOrThrow
import com.fluffy.comment.application.request.CreateExamCommentRequest
import com.fluffy.comment.application.request.DeleteExamCommentRequest
import com.fluffy.comment.application.response.CreateExamCommentResponse
import com.fluffy.comment.domain.ExamComment
import com.fluffy.comment.domain.ExamCommentRepository
import com.fluffy.comment.domain.findByIdOrThrow
import com.fluffy.exam.domain.ExamRepository
import com.fluffy.exam.domain.findByIdOrThrow
import com.fluffy.global.exception.BadRequestException
import com.fluffy.global.exception.ForbiddenException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ExamCommentService(
    private val memberRepository: MemberRepository,
    private val examRepository: ExamRepository,
    private val examCommentRepository: ExamCommentRepository
) {

    @Transactional
    fun createComment(request: CreateExamCommentRequest): CreateExamCommentResponse {
        val member = memberRepository.findByIdOrThrow(request.memberId)
        val exam = examRepository.findByIdOrThrow(request.examId)

        if (exam.isNotPublished()) {
            throw BadRequestException("출제되지 않은 시험에는 댓글을 작성할 수 없습니다.")
        }

        val examComment = request.parentCommentId?.let {
            createReplyComment(request.content, request.memberId, it)
        } ?: createRootComment(request.content, request.examId, request.memberId)

        return CreateExamCommentResponse.of(examComment, member)
    }

    @Transactional
    fun deleteComment(request: DeleteExamCommentRequest): Long {
        val examComment = examCommentRepository.findByIdOrThrow(request.commentId)

        if (examComment.isNotWrittenBy(request.memberId)) {
            throw ForbiddenException("댓글 작성자만 삭제할 수 있습니다.")
        }

        examComment.delete()
        return examComment.id
    }

    private fun createRootComment(content: String, examId: Long, memberId: Long): ExamComment {
        val rootExamComment = ExamComment.create(
            content = content,
            examId = examId,
            memberId = memberId
        )

        return examCommentRepository.save(rootExamComment)
    }

    private fun createReplyComment(content: String, memberId: Long, parentCommentId: Long): ExamComment {
        val parentExamComment = examCommentRepository.findByIdOrThrow(parentCommentId)
        val replyExamComment = parentExamComment.reply(content = content, memberId = memberId)

        return examCommentRepository.save(replyExamComment)
    }
}