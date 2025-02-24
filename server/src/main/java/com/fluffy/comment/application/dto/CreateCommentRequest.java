package com.fluffy.comment.application.dto;

public record CreateCommentRequest(
        String content,
        Long examId,
        Long memberId,
        Long parentCommentId
) {
}
