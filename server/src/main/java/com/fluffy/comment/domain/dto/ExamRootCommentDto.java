package com.fluffy.comment.domain.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;

public record ExamRootCommentDto(
        Long id,
        String content,
        AuthorDto author,
        Long replyCount,
        boolean isDeleted,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    private static final LocalDateTime EPOCH_TIME = LocalDateTime.of(1970, 1, 1, 0, 0, 0);

    @QueryProjection
    public ExamRootCommentDto {
    }

    public ExamRootCommentDto asDeleted() {
        return new ExamRootCommentDto(
                id, // 삭제된 댓글에 답글이 있을 경우 삭제된 댓글을 이용해 답글들을 조회하기 위해 id를 마스킹 하지 않음.
                "",
                AuthorDto.EMPTY,
                replyCount,
                true,
                EPOCH_TIME,
                EPOCH_TIME
        );
    }
}
