package com.fluffy.comment.ui.response

import com.fluffy.comment.domain.dto.ExamReplyCommentDto

data class ExamReplyCommentsWebResponse(
    val replies: List<ExamReplyCommentDto>
)
