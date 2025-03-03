package com.fluffy.comment.domain.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;

public record ExamReplyCommentDto(
        Long id,
        String content,
        AuthorDto author,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    @QueryProjection
    public ExamReplyCommentDto {
    }
}