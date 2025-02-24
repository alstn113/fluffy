package com.fluffy.comment.application.dto;

import com.fluffy.auth.domain.Member;
import com.fluffy.comment.domain.Comment;
import java.time.LocalDateTime;

public record CreateCommentResponse(
        Long id,
        String content,
        Long examId,
        Long parentCommentId,
        AuthorResponse author,
        LocalDateTime createdAt
) {

    public static CreateCommentResponse of(Comment comment, Member member) {
        return new CreateCommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getExamId(),
                comment.getParentCommentId(),
                AuthorResponse.from(member),
                comment.getCreatedAt()
        );
    }
}
