package com.fluffy.comment.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.fluffy.global.exception.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CommentTest {

    @Test
    @DisplayName("댓글을 생성할 수 있다.")
    void create() {
        // given
        String content = "댓글 내용";
        Long memberId = 1L;
        Long examId = 1L;

        // when
        Comment comment = Comment.create(content, memberId, examId);

        // then
        assertAll(
                () -> assertThat(comment.getContent()).isEqualTo(content),
                () -> assertThat(comment.getMemberId()).isEqualTo(memberId),
                () -> assertThat(comment.getExamId()).isEqualTo(examId),
                () -> assertThat(comment.getParentCommentId()).isNull(),
                () -> assertThat(comment.isDeleted()).isFalse()
        );
    }

    @Test
    @DisplayName("댓글을 논리적으로 삭제할 수 있다.")
    void delete() {
        // given
        Comment comment = Comment.create("댓글 내용", 1L, 1L);

        // when
        comment.delete();

        // then
        assertThat(comment.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("삭제된 댓글은 삭제할 수 없다.")
    void delete_deletedComment() {
        // given
        Comment comment = Comment.create("댓글 내용", 1L, 1L);
        comment.delete();

        // when, then
        assertThatThrownBy(comment::delete)
                .isInstanceOf(BadRequestException.class)
                .hasMessage("이미 삭제된 댓글입니다.");
    }

    @Test
    @DisplayName("댓글에 답글을 작성할 수 있다.")
    void reply() {
        // given
        Comment root = Comment.create("댓글 내용", 1L, 1L);

        // when
        Comment reply = root.reply("답글 내용", 2L);

        // then
        assertThat(reply.getParentCommentId()).isEqualTo(root.getId());
    }

    @Test
    @DisplayName("삭제된 댓글에는 답글을 작성할 수 없다.")
    void reply_deletedComment() {
        // given
        Comment root = Comment.create("댓글 내용", 1L, 1L);
        root.delete();

        // when, then
        assertThatThrownBy(() -> root.reply("답글 내용", 2L))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("삭제된 댓글에는 답글을 작성할 수 없습니다.");
    }

    @Test
    @DisplayName("답글에는 답글을 작성할 수 없다.")
    void reply_replyComment() {
        // given
        Long parentCommentId = 1L;
        Comment reply = new Comment("답글 내용", 1L, 1L, parentCommentId);

        // when, then
        assertThatThrownBy(() -> reply.reply("답글 내용", 1L))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("답글에는 답글을 작성할 수 없습니다.");
    }
}
