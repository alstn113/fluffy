package com.fluffy.comment.application.request

data class DeleteExamCommentRequest(
    val commentId: Long,
    val memberId: Long
)
