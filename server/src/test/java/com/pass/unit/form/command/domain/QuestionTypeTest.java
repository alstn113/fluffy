package com.pass.unit.form.command.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.pass.form.command.domain.QuestionType;
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
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Unknown question type: NOT_EXIST");
    }
}
