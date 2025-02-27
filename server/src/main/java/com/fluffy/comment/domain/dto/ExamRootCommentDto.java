package com.fluffy.comment.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExamRootCommentDto {

    private static final LocalDateTime EPOCH_TIME = LocalDateTime.of(1970, 1, 1, 0, 0, 0);

    private Long id;
    private String content;
    private AuthorDto author;
    private Long replyCount;
    @JsonProperty("isDeleted")
    private boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @QueryProjection
    public ExamRootCommentDto(
            Long id,
            String content,
            AuthorDto author,
            Long replyCount,
            boolean isDeleted,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.replyCount = replyCount;
        this.isDeleted = isDeleted;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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