package com.fluffy.reaction.domain;

import com.fluffy.auth.domain.Member;
import com.fluffy.auth.domain.MemberRepository;
import com.fluffy.support.AbstractIntegrationTest;
import com.fluffy.support.data.MemberTestData;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class LikeServiceTest extends AbstractIntegrationTest {

    @Autowired
    private LikeService likeService;

    @Autowired
    private ReactionRepository reactionRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("좋아요를 할 수 있다.")
    void like() {
        // given
        Member member = createMember();
        Reaction reaction = new Reaction("EXAM", 1L, member.getId(), ReactionType.LIKE);
        reactionRepository.save(reaction);

        // when
        likeService.like(new Like(LikeTarget.EXAM, 1L), member.getId());

        // then
        Reaction savedReaction = reactionRepository.findById(reaction.getId()).orElseThrow();
        assertThat(savedReaction.getStatus()).isEqualTo(ReactionStatus.ACTIVE);
    }

    @Test
    @DisplayName("이전에 좋아요를 취소한 상태에서는 좋아요를 새로 생성하지 않고, 기존 좋아요를 활성화한다.")
    void like_alreadyUnliked() {
        // given
        Member member = createMember();
        Reaction reaction = new Reaction("EXAM", 1L, member.getId(), ReactionType.LIKE);
        reaction.delete();
        reactionRepository.save(reaction);

        // when
        likeService.like(new Like(LikeTarget.EXAM, 1L), member.getId());

        // then
        Reaction savedReaction = reactionRepository.findById(reaction.getId()).orElseThrow();
        assertThat(savedReaction.getStatus()).isEqualTo(ReactionStatus.ACTIVE);
    }

    @Test
    @DisplayName("좋아요를 취소할 수 있다.")
    void unlike() {
        // given
        Member member = createMember();
        Reaction reaction = new Reaction("EXAM", 1L, member.getId(), ReactionType.LIKE);
        reactionRepository.save(reaction);

        // when
        likeService.removeLike(new Like(LikeTarget.EXAM, 1L), member.getId());

        // then
        Reaction savedReaction = reactionRepository.findById(reaction.getId()).orElseThrow();
        assertThat(savedReaction.getStatus()).isEqualTo(ReactionStatus.DELETED);
    }

    @Test
    @DisplayName("좋아요를 한 상태에서만 좋아요를 취소할 수 있다.")
    void unlike_notLiked() {
        // given
        Member member = createMember();
        Reaction reaction = new Reaction("EXAM", 1L, member.getId(), ReactionType.LIKE);
        reaction.delete();
        reactionRepository.save(reaction);

        // when
        likeService.removeLike(new Like(LikeTarget.EXAM, 1L), member.getId());

        // then
        Reaction savedReaction = reactionRepository.findById(reaction.getId()).orElseThrow();
        assertThat(savedReaction.getStatus()).isEqualTo(ReactionStatus.DELETED);
    }

    private Member createMember() {
        Member member = MemberTestData.defaultMember().build();
        return memberRepository.save(member);
    }
}
