package com.fluffy.comment.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.fluffy.auth.domain.Member;
import com.fluffy.auth.domain.MemberRepository;
import com.fluffy.comment.domain.dto.ExamReplyCommentDto;
import com.fluffy.comment.domain.dto.ExamRootCommentDto;
import com.fluffy.exam.domain.Exam;
import com.fluffy.exam.domain.ExamRepository;
import com.fluffy.exam.domain.Question;
import com.fluffy.support.AbstractIntegrationTest;
import com.fluffy.support.data.MemberTestData;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ExamCommentRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private ExamCommentRepository examCommentRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ExamRepository examRepository;

    @Test
    @DisplayName("루트 댓글들을 조회할 수 있다.")
    void findRootComments() {
        // given
        Member member1 = createMember(1L);
        Member member2 = createMember(2L);
        Exam exam = createExam(member1);

        /*
        댓글 구조

        root1
            - root1reply1
            - root1reply2
        root2
        root3
            - root3reply1
         */

        ExamComment root1 = examCommentRepository.save(ExamComment.create("댓글1", exam.getId(), member1.getId()));
        examCommentRepository.save(root1.reply("댓글1-답글1", member2.getId()));
        examCommentRepository.save(root1.reply("댓글1-답글2", member1.getId()));
        ExamComment root2 = examCommentRepository.save(ExamComment.create("댓글2", exam.getId(), member2.getId()));
        ExamComment root3 = examCommentRepository.save(ExamComment.create("댓글3", exam.getId(), member2.getId()));
        examCommentRepository.save(root3.reply("댓글3-답글1", member1.getId()));

        // when
        List<ExamRootCommentDto> rootComments = examCommentRepository.findRootComments(exam.getId());

        // then
        assertAll(
                () -> assertThat(rootComments).hasSize(3),

                () -> assertThat(rootComments.get(0).getId()).isEqualTo(root3.getId()),
                () -> assertThat(rootComments.get(0).getContent()).isEqualTo("댓글3"),
                () -> assertThat(rootComments.get(0).getAuthor().getName()).isEqualTo(member2.getName()),
                () -> assertThat(rootComments.get(0).getReplyCount()).isEqualTo(1),

                () -> assertThat(rootComments.get(1).getId()).isEqualTo(root2.getId()),
                () -> assertThat(rootComments.get(1).getContent()).isEqualTo("댓글2"),
                () -> assertThat(rootComments.get(1).getAuthor().getName()).isEqualTo(member2.getName()),
                () -> assertThat(rootComments.get(1).getReplyCount()).isZero(),

                () -> assertThat(rootComments.get(2).getId()).isEqualTo(root1.getId()),
                () -> assertThat(rootComments.get(2).getContent()).isEqualTo("댓글1"),
                () -> assertThat(rootComments.get(2).getAuthor().getName()).isEqualTo(member1.getName()),
                () -> assertThat(rootComments.get(2).getReplyCount()).isEqualTo(2)
        );
    }

    @Test
    @DisplayName("루트 댓글들을 조회 시 삭제된 댓글이면서, 답글이 없는 댓글은 제외한다.")
    void findRootCommentsWithDeletedComment() {
        // given
        Member member1 = createMember(1L);
        Exam exam = createExam(member1);

        /*
        댓글 구조

        root1(deleted) - 제외
        root2(deleted)
            - root2reply1
        root3
            - root3reply1(deleted)
         */

        ExamComment roo1 = ExamComment.create("댓글1", exam.getId(), member1.getId());
        roo1.delete();
        examCommentRepository.save(roo1);

        ExamComment root2 = ExamComment.create("댓글2", exam.getId(), member1.getId());
        examCommentRepository.save(root2);
        ExamComment root2reply1 = root2.reply("댓글2-답글1", member1.getId());
        examCommentRepository.save(root2reply1);
        root2.delete();
        examCommentRepository.save(root2);

        ExamComment root3 = examCommentRepository.save(ExamComment.create("댓글3", exam.getId(), member1.getId()));
        ExamComment root3reply1 = root3.reply("댓글3-답글1", member1.getId());
        root3reply1.delete();
        examCommentRepository.save(root3reply1);

        // when
        List<ExamRootCommentDto> rootComments = examCommentRepository.findRootComments(exam.getId());

        // then
        assertAll(
                () -> assertThat(rootComments).hasSize(2),


                () -> assertThat(rootComments.get(0).getId()).isEqualTo(root3.getId()),
                () -> assertThat(rootComments.get(0).isDeleted()).isFalse(),
                () -> assertThat(rootComments.get(0).getReplyCount()).isZero(),

                () -> assertThat(rootComments.get(1).getId()).isEqualTo(root2.getId()),
                () -> assertThat(rootComments.get(1).isDeleted()).isTrue(),
                () -> assertThat(rootComments.get(1).getReplyCount()).isEqualTo(1)
        );
    }

    @Test
    @DisplayName("루트 댓글에 대한 답글들을 조회할 수 있다.")
    void findRootCommentWithReplies() {
        // given
        Member member1 = createMember(1L);
        Exam exam = createExam(member1);

        /*
        댓글 구조

        root1
            - root1reply1
            - root1reply2
         */

        ExamComment root1 = examCommentRepository.save(ExamComment.create("댓글1", exam.getId(), member1.getId()));
        examCommentRepository.save(root1.reply("댓글1-답글1", member1.getId()));
        examCommentRepository.save(root1.reply("댓글1-답글2", member1.getId()));

        // when
        List<ExamReplyCommentDto> replies = examCommentRepository.findRootCommentWithReplies(root1.getId());

        // then
        assertAll(
                () -> assertThat(replies).hasSize(2),

                () -> assertThat(replies.get(0).getContent()).isEqualTo("댓글1-답글2"),
                () -> assertThat(replies.get(0).getAuthor().getName()).isEqualTo(member1.getName()),

                () -> assertThat(replies.get(1).getContent()).isEqualTo("댓글1-답글1"),
                () -> assertThat(replies.get(1).getAuthor().getName()).isEqualTo(member1.getName())
        );
    }

    @Test
    @DisplayName("루트 댓글에 대한 답글들을 조회 시 삭제된 답글은 제외한다.")
    void findRootCommentWithRepliesWithDeletedReply() {
        // given
        Member member1 = createMember(1L);
        Exam exam = createExam(member1);

        /*
        댓글 구조

        root1
            - root1reply1(deleted) - 제외
            - root1reply2
         */

        ExamComment root1 = examCommentRepository.save(ExamComment.create("댓글1", exam.getId(), member1.getId()));
        ExamComment root1reply1 = root1.reply("댓글1-답글1", member1.getId());
        root1reply1.delete();
        examCommentRepository.save(root1reply1);
        examCommentRepository.save(root1.reply("댓글1-답글2", member1.getId()));

        // when
        List<ExamReplyCommentDto> replies = examCommentRepository.findRootCommentWithReplies(root1.getId());

        // then
        assertAll(
                () -> assertThat(replies).hasSize(1),

                () -> assertThat(replies.get(0).getContent()).isEqualTo("댓글1-답글2"),
                () -> assertThat(replies.get(0).getAuthor().getName()).isEqualTo(member1.getName())
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
