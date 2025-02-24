package com.fluffy.comment.application;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.fluffy.auth.domain.Member;
import com.fluffy.auth.domain.MemberRepository;
import com.fluffy.comment.application.dto.CreateCommentRequest;
import com.fluffy.comment.application.dto.CreateCommentResponse;
import com.fluffy.comment.application.dto.DeleteCommentRequest;
import com.fluffy.comment.domain.Comment;
import com.fluffy.comment.domain.CommentRepository;
import com.fluffy.exam.domain.Exam;
import com.fluffy.exam.domain.ExamRepository;
import com.fluffy.exam.domain.Question;
import com.fluffy.global.exception.ForbiddenException;
import com.fluffy.support.AbstractIntegrationTest;
import com.fluffy.support.data.MemberTestData;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CommentServiceTest extends AbstractIntegrationTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    @DisplayName("댓글을 작성할 수 있다.")
    void createRootComment() {
        // given
        Member member = createMember();
        Exam exam = createExam(member, true);

        // when
        CreateCommentRequest request = new CreateCommentRequest("댓글", exam.getId(), member.getId(), null);
        CreateCommentResponse response = commentService.createComment(request);

        // then
        assertAll(
                () -> assertThat(response.id()).isNotNull(),
                () -> assertThat(response.content()).isEqualTo("댓글"),
                () -> assertThat(response.examId()).isEqualTo(exam.getId()),
                () -> assertThat(response.parentCommentId()).isNull(),
                () -> assertThat(response.author().id()).isEqualTo(member.getId()),
                () -> assertThat(response.author().name()).isEqualTo(member.getName()),
                () -> assertThat(response.author().avatarUrl()).isEqualTo(member.getAvatarUrl()),
                () -> assertThat(commentRepository.findAll()).hasSize(1)
        );
    }

    @Test
    @DisplayName("출제되지 않은 시험에는 댓글을 작성할 수 없다.")
    void createCommentWithNotPublishedExam() {
        // given
        Member member = createMember();
        Exam exam = createExam(member, false);

        // when, then
        CreateCommentRequest request = new CreateCommentRequest("댓글", exam.getId(), member.getId(), null);

        assertThatThrownBy(() -> commentService.createComment(request))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage("출제되지 않은 시험에는 댓글을 작성할 수 없습니다.");

    }

    @Test
    @DisplayName("답글을 작성할 수 있다.")
    void createReplyComment() {
        // given
        Member member = createMember();
        Exam exam = createExam(member, true);
        Comment root = commentRepository.save(Comment.create("댓글", exam.getId(), member.getId()));

        // when
        CreateCommentRequest request = new CreateCommentRequest("답글", exam.getId(), member.getId(), root.getId());
        CreateCommentResponse response = commentService.createComment(request);

        // then
        assertAll(
                () -> assertThat(response.parentCommentId()).isEqualTo(root.getId()),
                () -> assertThat(commentRepository.findAll()).hasSize(2)
        );
    }

    @Test
    @DisplayName("댓글을 삭제할 수 있다.")
    void deleteComment() {
        // given
        Member member = createMember();
        Exam exam = createExam(member, true);
        Comment comment = commentRepository.save(Comment.create("댓글", exam.getId(), member.getId()));

        // when
        DeleteCommentRequest deleteCommentRequest = new DeleteCommentRequest(comment.getId(), member.getId());
        commentService.deleteComment(deleteCommentRequest);

        // then
        Comment deletedComment = commentRepository.findByIdOrThrow(comment.getId());
        assertThat(deletedComment.isDeleted()).isTrue();
    }

    @Test
    @DisplayName("댓글은 해당 댓글의 작성자만 삭제할 수 있다.")
    void deleteCommentWithNotWriter() {
        // given
        Member member = createMember();
        Exam exam = createExam(member, true);
        Comment comment = commentRepository.save(Comment.create("댓글", exam.getId(), member.getId()));

        // when, then
        Long anotherMemberId = member.getId() + 1;
        DeleteCommentRequest deleteCommentRequest = new DeleteCommentRequest(comment.getId(), anotherMemberId);

        assertThatThrownBy(() -> commentService.deleteComment(deleteCommentRequest))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage("댓글 작성자만 삭제할 수 있습니다.");
    }

    private Member createMember() {
        Member member = MemberTestData.defaultMember().build();

        return memberRepository.save(member);
    }

    private Exam createExam(Member member, boolean isPublished) {
        Exam exam = Exam.create("시험", member.getId());
        exam.updateQuestions(List.of(Question.shortAnswer("질문", "설명", "정답")));

        if (isPublished) {
            exam.publish();
        }

        return examRepository.save(exam);
    }
}
