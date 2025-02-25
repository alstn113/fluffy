package com.fluffy.comment.application.dto;

import com.fluffy.auth.domain.Member;
import com.fluffy.comment.domain.ExamComment;
import java.time.LocalDateTime;

public record CreateExamCommentResponse(
        Long id,
        String content,
        Long examId,
        Long parentCommentId,
        AuthorResponse author,
        LocalDateTime createdAt
) {

    public static CreateExamCommentResponse of(ExamComment examComment, Member member) {
        return new CreateExamCommentResponse(
                examComment.getId(),
                examComment.getContent(),
                examComment.getExamId(),
                examComment.getParentCommentId(),
                AuthorResponse.from(member),
                examComment.getCreatedAt()
        );
    }
}
