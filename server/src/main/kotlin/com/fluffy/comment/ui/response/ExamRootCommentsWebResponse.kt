package com.fluffy.comment.ui.response

import com.fluffy.comment.domain.dto.ExamRootCommentDto

data class ExamRootCommentsWebResponse(
    val comments: List<ExamRootCommentDto>,
)
