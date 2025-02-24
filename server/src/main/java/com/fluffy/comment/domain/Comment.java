package com.fluffy.comment.domain;


import com.fluffy.global.exception.BadRequestException;
import com.fluffy.global.persistence.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Comment extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "exam_id", nullable = false)
    private Long examId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "parent_comment_id")
    private Long parentCommentId;

    @Column
    private LocalDateTime deletedAt;

    public static Comment create(String content, Long examId, Long memberId) {
        return new Comment(content, examId, memberId, null);
    }

    public Comment(String content, Long examId, Long memberId, Long parentCommentId) {
        this(null, content, examId, memberId, parentCommentId);
    }

    public Comment(Long id, String content, Long examId, Long memberId, Long parentCommentId) {
        this.id = id;
        this.content = content;
        this.examId = examId;
        this.memberId = memberId;
        this.parentCommentId = parentCommentId;
    }

    public Comment reply(String content, Long memberId) {
        if (isDeleted()) {
            throw new BadRequestException("삭제된 댓글에는 답글을 작성할 수 없습니다.");
        }

        if (isReply()) {
            throw new BadRequestException("답글에는 답글을 작성할 수 없습니다.");
        }

        return new Comment(content, examId, memberId, id);
    }

    public void delete() {
        if (isDeleted()) {
            throw new BadRequestException("이미 삭제된 댓글입니다.");
        }

        deletedAt = LocalDateTime.now();
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }

    public boolean isReply() {
        return parentCommentId != null;
    }

    public boolean isNotWrittenBy(Long memberId) {
        return !this.memberId.equals(memberId);
    }
}
