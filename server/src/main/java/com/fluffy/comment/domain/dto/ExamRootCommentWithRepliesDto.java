package com.fluffy.comment.domain.dto;

import java.util.List;

public record ExamRootCommentWithRepliesDto(
        Long id,
        List<ExamReplyCommentDto> replies
) {
}
