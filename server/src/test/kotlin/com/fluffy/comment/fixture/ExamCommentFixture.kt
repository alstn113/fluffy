package com.fluffy.comment.fixture

import com.fluffy.comment.domain.ExamComment

object ExamCommentFixture {

    fun create(
        id: Long = 0L,
        content: String = "댓글입니다.",
        examId: Long = 1L,
        parentCommentId: Long? = null,
        memberId: Long = 1L,
    ): ExamComment {
        return ExamComment(
            id = id,
            content = content,
            examId = examId,
            parentCommentId = parentCommentId,
            memberId = memberId
        )
    }
}