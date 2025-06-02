package com.fluffy.comment.application.response

import com.fluffy.auth.domain.Member
import com.fluffy.comment.domain.ExamComment
import java.time.LocalDateTime

data class CreateExamCommentResponse(
    val id: Long,
    val content: String,
    val examId: Long,
    val parentCommentId: Long?,
    val author: AuthorResponse,
    val createdAt: LocalDateTime,
) {

    companion object {

        fun of(examComment: ExamComment, member: Member): CreateExamCommentResponse {
            return CreateExamCommentResponse(
                examComment.id,
                examComment.content,
                examComment.examId,
                examComment.parentCommentId,
                AuthorResponse.from(member),
                examComment.createdAt,
            )
        }
    }
}
