package com.pass.unit.exam.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.pass.exam.domain.QuestionType;
import com.pass.global.exception.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class QuestionTypeTest {

    @Test
    @DisplayName("질문 유형 이름으로 질문 유형을 찾을 수 있다.")
    void from() {
        // given
        String questionTypeName = "SHORT_ANSWER";

        // when
        QuestionType foundQuestionType = QuestionType.from(questionTypeName);

        // then
        assertThat(foundQuestionType).isEqualTo(QuestionType.SHORT_ANSWER);
    }

    @Test
    @DisplayName("질문 유형 이름으로 질문 유형을 찾을 수 없을 경우 예외를 발생시킨다.")
    void fromFailWhenQuestionTypeNotFound() {
        // given
        String questionTypeName = "NOT_EXIST";

        // when & then
        assertThatThrownBy(() -> QuestionType.from(questionTypeName))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 질문 유형입니다. 질문 유형: NOT_EXIST");
    }
}
