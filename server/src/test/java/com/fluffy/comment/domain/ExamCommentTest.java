package com.fluffy.comment.domain;

import com.fluffy.global.exception.BadRequestException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ExamCommentTest {

    @Test
    @DisplayName("댓글을 생성할 수 있다.")
    void create() {
        // given
        String content = "댓글 내용";
        Long memberId = 1L;
        Long examId = 1L;

        // when
        ExamComment examComment = ExamComment.create(content, memberId, examId);

        // then
        assertAll(
                () -> assertThat(examComment.getContent()).isEqualTo(content),
                () -> assertThat(examComment.getMemberId()).isEqualTo(memberId),
                () -> assertThat(examComment.getExamId()).isEqualTo(examId),
                () -> assertThat(examComment.getParentCommentId()).isNull(),
                () -> assertThat(examComment.isDeleted()).isFalse()
        );
    }

    @Test
    @DisplayName("댓글을 논리적으로 삭제할 수 있다.")
    void delete() {
        // given
        ExamComment examComment = ExamComment.create("댓글 내용", 1L, 1L);

        // when
        examComment.delete();

        // then
        assertThat(examComment.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("삭제된 댓글은 삭제할 수 없다.")
    void delete_deletedComment() {
        // given
        ExamComment examComment = ExamComment.create("댓글 내용", 1L, 1L);
        examComment.delete();

        // when, then
        assertThatThrownBy(examComment::delete)
                .isInstanceOf(BadRequestException.class)
                .hasMessage("이미 삭제된 댓글입니다.");
    }

    @Test
    @DisplayName("댓글에 답글을 작성할 수 있다.")
    void reply() {
        // given
        ExamComment root = ExamComment.create("댓글 내용", 1L, 1L);

        // when
        ExamComment reply = root.reply("답글 내용", 2L);

        // then
        assertThat(reply.getParentCommentId()).isEqualTo(root.getId());
    }

    @Test
    @DisplayName("삭제된 댓글에는 답글을 작성할 수 없다.")
    void reply_deletedComment() {
        // given
        ExamComment root = ExamComment.create("댓글 내용", 1L, 1L);
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
        ExamComment reply = new ExamComment("답글 내용", 1L, 1L, parentCommentId);

        // when, then
        assertThatThrownBy(() -> reply.reply("답글 내용", 1L))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("답글에는 답글을 작성할 수 없습니다.");
    }
}
