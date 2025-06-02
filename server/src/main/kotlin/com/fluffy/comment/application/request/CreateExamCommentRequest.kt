package com.fluffy.comment.application.request

data class CreateExamCommentRequest(
    val content: String,
    val examId: Long,
    val memberId: Long,
    val parentCommentId: Long?,
)
