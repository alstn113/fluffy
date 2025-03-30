package com.fluffy.comment.ui.request

import com.fluffy.comment.application.request.CreateExamCommentRequest
import jakarta.validation.constraints.NotBlank

data class CreateExamCommentWebRequest(
    @field:NotBlank val content: String,
    val parentCommentId: Long?
) {

    fun toAppRequest(examId: Long, memberId: Long): CreateExamCommentRequest {
        return CreateExamCommentRequest(
            content = content,
            examId = examId,
            memberId = memberId,
            parentCommentId = parentCommentId
        )
    }
}
