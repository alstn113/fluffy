package com.fluffy.comment.application.dto;

public record CreateExamCommentRequest(
        String content,
        Long examId,
        Long memberId,
        Long parentCommentId
) {
}
