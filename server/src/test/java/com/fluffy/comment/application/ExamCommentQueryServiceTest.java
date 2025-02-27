package com.fluffy.comment.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.fluffy.auth.domain.Member;
import com.fluffy.auth.domain.MemberRepository;
import com.fluffy.comment.domain.ExamComment;
import com.fluffy.comment.domain.ExamCommentRepository;
import com.fluffy.comment.domain.dto.ExamReplyCommentDto;
import com.fluffy.comment.domain.dto.ExamRootCommentDto;
import com.fluffy.exam.domain.Exam;
import com.fluffy.exam.domain.ExamRepository;
import com.fluffy.exam.domain.Question;
import com.fluffy.global.exception.NotFoundException;
import com.fluffy.support.AbstractIntegrationTest;
import com.fluffy.support.data.MemberTestData;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ExamCommentQueryServiceTest extends AbstractIntegrationTest {

    @Autowired
    private ExamCommentQueryService examCommentQueryService;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ExamCommentRepository examCommentRepository;

    @Test
    @DisplayName("루트 댓글들을 조회할 수 있다.")
    void getRootComments() {
        // given
        Member member1 = createMember(1L);
        Member member2 = createMember(2L);
        Exam exam = createExam(member1);

        ExamComment root1 = examCommentRepository.save(ExamComment.create("댓글1", exam.getId(), member1.getId()));
        examCommentRepository.save(root1.reply("댓글1-답글1", member2.getId()));
        ExamComment root2 = examCommentRepository.save(ExamComment.create("댓글2", exam.getId(), member2.getId()));

        // when
        List<ExamRootCommentDto> rootComments = examCommentQueryService.getRootComments(exam.getId());

        // then
        assertAll(
                () -> assertThat(rootComments).hasSize(2),
                () -> assertThat(rootComments.get(0).getId()).isEqualTo(root1.getId()),
                () -> assertThat(rootComments.get(0).getReplyCount()).isEqualTo(1),
                () -> assertThat(rootComments.get(1).getId()).isEqualTo(root2.getId()),
                () -> assertThat(rootComments.get(1).getReplyCount()).isZero()
        );
    }

    @Test
    @DisplayName("루트 댓글 조회 시 시험이 존재하지 않으면 예외를 반환한다.")
    void getRootCommentsWithNonExistentExam() {
        // given
        Long nonExistentExamId = -1L;

        // when & then
        assertThatThrownBy(() -> examCommentQueryService.getRootComments(nonExistentExamId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 시험입니다. 시험 식별자: -1");
    }

    @Test
    @DisplayName("루트 댓글 조회 시 삭제된 댓글이지만 답글이 있는 경우는 내용을 가려서 반환한다.")
    void getRootCommentsWithDeletedComment() {
        // given
        Member member1 = createMember(1L);
        Member member2 = createMember(2L);
        Exam exam = createExam(member1);

        /*
        댓글 구조

        root1 (deleted)
            - root1reply1
        root2 (deleted)
        root3
            - root3reply1 (deleted)
         */
        ExamComment root1 = examCommentRepository.save(ExamComment.create("댓글1", exam.getId(), member1.getId()));
        examCommentRepository.save(root1.reply("댓글1-답글1", member2.getId()));
        root1.delete();
        examCommentRepository.save(root1);

        ExamComment root2 = ExamComment.create("댓글2", exam.getId(), member1.getId());
        root2.delete();
        examCommentRepository.save(root2);

        ExamComment root3 = examCommentRepository.save(ExamComment.create("댓글3", exam.getId(), member1.getId()));
        ExamComment root3reply1 = root3.reply("댓글3-답글1", member2.getId());
        root3reply1.delete();
        examCommentRepository.save(root3reply1);

        // when
        List<ExamRootCommentDto> rootComments = examCommentQueryService.getRootComments(exam.getId());

        // then
        assertAll(
                () -> assertThat(rootComments).hasSize(2),

                () -> assertThat(rootComments.get(0).getId()).isEqualTo(root1.getId()),
                () -> assertThat(rootComments.get(0).getContent()).isEmpty(),
                () -> assertThat(rootComments.get(0).isDeleted()).isTrue(),
                () -> assertThat(rootComments.get(0).getReplyCount()).isEqualTo(1),

                () -> assertThat(rootComments.get(1).getId()).isEqualTo(root3.getId()),
                () -> assertThat(rootComments.get(1).getContent()).isEqualTo("댓글3"),
                () -> assertThat(rootComments.get(1).isDeleted()).isFalse(),
                () -> assertThat(rootComments.get(1).getReplyCount()).isZero()
        );
    }

    @Test
    @DisplayName("루트 댓글에 달린 답글들을 조회할 수 있다.")
    void getReplyComments() {
        // given
        Member member1 = createMember(1L);
        Member member2 = createMember(2L);
        Exam exam = createExam(member1);

        ExamComment root1 = examCommentRepository.save(ExamComment.create("댓글1", exam.getId(), member1.getId()));
        ExamComment root1reply1 = examCommentRepository.save(root1.reply("댓글1-답글1", member2.getId()));
        ExamComment root1reply2 = examCommentRepository.save(root1.reply("댓글1-답글2", member1.getId()));
        ExamComment root1reply3 = examCommentRepository.save(root1.reply("댓글1-답글3", member1.getId()));

        root1reply2.delete();
        examCommentRepository.save(root1reply2);

        // when
        List<ExamReplyCommentDto> replies = examCommentQueryService.getReplyComments(root1.getId());

        // then
        assertAll(
                () -> assertThat(replies).hasSize(2),
                () -> assertThat(replies.get(0).getId()).isEqualTo(root1reply1.getId()),
                () -> assertThat(replies.get(1).getId()).isEqualTo(root1reply3.getId())
        );
    }

    @Test
    @DisplayName("루트 댓글에 달린 답글들을 조회 시 시험이 존재하지 않으면 예외를 반환한다.")
    void getReplyCommentsWithNonExistentExam() {
        // given
        Long nonExistentCommentId = -1L;

        // when & then
        assertThatThrownBy(() -> examCommentQueryService.getReplyComments(nonExistentCommentId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 댓글입니다. 댓글 식별자: -1");
    }

    @Test
    @DisplayName("루트 댓글에 달린 답글들을 조회 시 루트 댓글이 삭제된 상태이나 답글이 존재하면 조회할 수 있다.")
    void getReplyCommentsWithDeletedRootCommentAndReplies() {
        // given
        Member member1 = createMember(1L);
        Member member2 = createMember(2L);
        Exam exam = createExam(member1);

        ExamComment root1 = examCommentRepository.save(ExamComment.create("댓글1", exam.getId(), member1.getId()));
        ExamComment root1reply1 = examCommentRepository.save(root1.reply("댓글1-답글1", member2.getId()));
        root1.delete();
        examCommentRepository.save(root1);

        // when
        List<ExamReplyCommentDto> replies = examCommentQueryService.getReplyComments(root1.getId());

        // then
        assertAll(
                () -> assertThat(replies).hasSize(1),
                () -> assertThat(replies.get(0).getId()).isEqualTo(root1reply1.getId())
        );
    }

    @Test
    @DisplayName("루트 댓글에 달린 답글들을 조회 시 루트 댓글이 삭제된 상태이고, 답글이 존재하지 않으면 예외를 반환한다.")
    void getReplyCommentsWithDeletedRootComment() {
        // given
        Member member1 = createMember(1L);
        Exam exam = createExam(member1);

        ExamComment root1 = examCommentRepository.save(ExamComment.create("댓글1", exam.getId(), member1.getId()));
        root1.delete();
        examCommentRepository.save(root1);

        // when & then
        Long root1Id = root1.getId();
        assertThatThrownBy(() -> examCommentQueryService.getReplyComments(root1Id))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 댓글입니다. 댓글 식별자: %d".formatted(root1Id));
    }

    @Test
    @DisplayName("루트 댓글들을 조회하고, 루트 댓글들의 식별자를 이용해서 답글들을 조회할 수 있다.")
    void getRootCommentsAndReplyComments() {
        // given
        Member member1 = createMember(1L);
        Member member2 = createMember(2L);
        Exam exam = createExam(member1);

        /*
        댓글 구조

        root1 (deleted)
            - root1reply1
        root2
            - root2reply1
         */
        ExamComment root1 = examCommentRepository.save(ExamComment.create("댓글1", exam.getId(), member1.getId()));
        ExamComment root1reply1 = examCommentRepository.save(root1.reply("댓글1-답글1", member2.getId()));
        root1.delete();
        examCommentRepository.save(root1);

        ExamComment root2 = examCommentRepository.save(ExamComment.create("댓글2", exam.getId(), member1.getId()));
        ExamComment root2reply1 = examCommentRepository.save(root2.reply("댓글2-답글1", member2.getId()));

        // when
        List<ExamRootCommentDto> rootComments = examCommentQueryService.getRootComments(exam.getId());

        List<ExamReplyCommentDto> root1replies = examCommentQueryService.getReplyComments(root1.getId());
        List<ExamReplyCommentDto> root2replies = examCommentQueryService.getReplyComments(root2.getId());

        // then
        assertAll(
                () -> assertThat(rootComments).hasSize(2),
                () -> assertThat(rootComments.get(0).getId()).isEqualTo(root1.getId()),
                () -> assertThat(rootComments.get(0).getContent()).isEmpty(),
                () -> assertThat(rootComments.get(0).isDeleted()).isTrue(),
                () -> assertThat(rootComments.get(0).getReplyCount()).isEqualTo(1),

                () -> assertThat(rootComments.get(1).getId()).isEqualTo(root2.getId()),
                () -> assertThat(rootComments.get(1).getContent()).isEqualTo("댓글2"),
                () -> assertThat(rootComments.get(1).isDeleted()).isFalse(),
                () -> assertThat(rootComments.get(1).getReplyCount()).isEqualTo(1),

                () -> assertThat(root1replies).hasSize(1),
                () -> assertThat(root1replies.get(0).getId()).isEqualTo(root1reply1.getId()),

                () -> assertThat(root2replies).hasSize(1),
                () -> assertThat(root2replies.get(0).getId()).isEqualTo(root2reply1.getId())
        );
    }

    private Member createMember(Long id) {
        Member member = MemberTestData.defaultMember().withId(id).build();
        return memberRepository.save(member);
    }

    private Exam createExam(Member member) {
        Exam exam = Exam.create("시험", member.getId());
        exam.updateQuestions(List.of(Question.shortAnswer("질문", "설명", "정답")));
        exam.publish();

        return examRepository.save(exam);
    }
}
