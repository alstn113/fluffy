package com.fluffy.comment.application.dto;

public record DeleteExamCommentRequest(
        Long commentId,
        Long memberId
) {
}
