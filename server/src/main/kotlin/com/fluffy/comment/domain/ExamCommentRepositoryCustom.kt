package com.fluffy.comment.domain

import com.fluffy.comment.domain.dto.ExamReplyCommentDto
import com.fluffy.comment.domain.dto.ExamRootCommentDto

interface ExamCommentRepositoryCustom {

    fun findRootComments(examId: Long): List<ExamRootCommentDto>

    fun findReplyComments(commentId: Long): List<ExamReplyCommentDto>
}