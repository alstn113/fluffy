package com.fluffy.comment.ui.dto;

import com.fluffy.comment.application.dto.CreateExamCommentRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CreateExamCommentWebRequest(
        @NotBlank String content,
        @Positive Long parentCommentId
) {

    public CreateExamCommentRequest toAppRequest(Long examId, Long memberId) {
        return new CreateExamCommentRequest(content, examId, memberId, parentCommentId);
    }
}
