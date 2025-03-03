package com.fluffy.exam.application;

import com.fluffy.auth.domain.Member;
import com.fluffy.auth.domain.MemberRepository;
import com.fluffy.exam.domain.Exam;
import com.fluffy.exam.domain.ExamRepository;
import com.fluffy.global.web.Accessor;
import com.fluffy.reaction.domain.LikeTarget;
import com.fluffy.reaction.domain.Reaction;
import com.fluffy.reaction.domain.ReactionRepository;
import com.fluffy.reaction.domain.ReactionStatus;
import com.fluffy.reaction.domain.ReactionType;
import com.fluffy.support.AbstractIntegrationTest;
import com.fluffy.support.data.MemberTestData;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ExamLikeServiceTest extends AbstractIntegrationTest {

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ExamLikeService examLikeService;

    @Autowired
    private ReactionRepository reactionRepository;

    @Test
    @DisplayName("시험에 좋아요를 할 수 있다.")
    void like() {
        // given
        Member member = createMember();
        Exam exam = createExam(member);
        Accessor accessor = new Accessor(member.getId());

        // when
        Long likeId = examLikeService.like(exam.getId(), accessor);

        // then
        Reaction reaction = reactionRepository.findById(likeId).orElseThrow();
        assertAll(
                () -> assertThat(likeId).isNotNull(),
                () -> assertThat(reaction.getType()).isEqualTo(ReactionType.LIKE),
                () -> assertThat(reaction.getTargetType()).isEqualTo(LikeTarget.EXAM.name()),
                () -> assertThat(reaction.getTargetId()).isEqualTo(exam.getId())
        );
    }

    @Test
    @DisplayName("시험에 좋아요를 취소할 수 있다.")
    void unlike() {
        // given
        Member member = createMember();
        Exam exam = createExam(member);
        Accessor accessor = new Accessor(member.getId());

        Reaction reaction = new Reaction(LikeTarget.EXAM.name(), exam.getId(), member.getId(), ReactionType.LIKE);
        reactionRepository.save(reaction);

        // when
        examLikeService.unlike(exam.getId(), accessor);

        // then
        Reaction deletedReaction = reactionRepository.findById(reaction.getId()).orElseThrow();
        assertThat(deletedReaction.getStatus()).isEqualTo(ReactionStatus.DELETED);
    }

    private Member createMember() {
        Member member = MemberTestData.defaultMember().build();
        return memberRepository.save(member);
    }

    private Exam createExam(Member member) {
        Exam exam = Exam.create("시험 제목", member.getId());
        return examRepository.save(exam);
    }
}
