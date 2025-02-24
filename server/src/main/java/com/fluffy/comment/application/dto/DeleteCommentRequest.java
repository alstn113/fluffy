package com.fluffy.comment.application.dto;

public record DeleteCommentRequest(
        Long commentId,
        Long memberId
) {
}
