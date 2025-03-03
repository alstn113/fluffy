package com.fluffy.reaction.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReactionTest {

    @Test
    @DisplayName("반응을 정상적으로 생성할 수 있다.")
    void create() {
        // when & then
        assertThatCode(() -> new Reaction("EXAM", 1L, 1L, ReactionType.LIKE))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("반응을 삭제 상태로 변경할 수 있다.")
    void delete() {
        // given
        Reaction reaction = new Reaction("EXAM", 1L, 1L, ReactionType.LIKE);

        // when
        reaction.delete();

        // then
        assertThat(reaction.getStatus()).isEqualTo(ReactionStatus.DELETED);
    }

    @Test
    @DisplayName("삭제 상태의 반응을 활성 상태로 변경할 수 있다.")
    void active() {
        // given
        Reaction reaction = new Reaction("EXAM", 1L, 1L, ReactionType.LIKE);
        reaction.delete();

        // when
        reaction.active();

        // then
        assertThat(reaction.getStatus()).isEqualTo(ReactionStatus.ACTIVE);
    }
}
