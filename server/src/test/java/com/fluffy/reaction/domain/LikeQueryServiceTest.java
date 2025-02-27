package com.fluffy.reaction.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.fluffy.auth.domain.Member;
import com.fluffy.auth.domain.MemberRepository;
import com.fluffy.support.AbstractIntegrationTest;
import com.fluffy.support.data.MemberTestData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class LikeQueryServiceTest extends AbstractIntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LikeQueryService likeQueryService;

    @Autowired
    private ReactionRepository reactionRepository;

    @Test
    @DisplayName("사용자가 특정 대상에 대해 좋아요를 했는지 여부를 조회할 수 있다.")
    void isLiked() {
        // given
        LikeTarget likeTarget = LikeTarget.EXAM;
        Long targetId = 1L;

        Member member = createMember();

        Reaction reaction = new Reaction(likeTarget.name(), targetId, member.getId(), ReactionType.LIKE);
        reactionRepository.save(reaction);

        // when
        boolean isLiked = likeQueryService.isLiked(new Like(likeTarget, targetId), member.getId());

        // then
        assertThat(isLiked).isTrue();
    }

    @Test
    @DisplayName("사용자가 특정 대상에 대해 좋아요를 하지 않았다면 거짓을 반환한다.")
    void isNotLiked() {
        // given
        LikeTarget likeTarget = LikeTarget.EXAM;
        Long targetId = 1L;

        Member member = createMember();

        // when
        boolean isLiked = likeQueryService.isLiked(new Like(likeTarget, targetId), member.getId());

        // then
        assertThat(isLiked).isFalse();
    }

    private Member createMember() {
        Member member = MemberTestData.defaultMember().build();
        return memberRepository.save(member);
    }
}
