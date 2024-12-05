package com.pass.unit.exam.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.pass.exam.domain.ExamDescription;
import com.pass.global.exception.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ExamDescriptionTest {

    @Test
    @DisplayName("시험 설명은 생성할 수 있다.")
    void create() {
        // given
        String description = "a".repeat(2000);

        // when
        ExamDescription examDescription = new ExamDescription(description);

        // then
        assertThat(examDescription.getValue()).isEqualTo(description);
    }

    @Test
    @DisplayName("시험 설명은 null일 수 없다.")
    void descriptionNotNull() {
        assertThatThrownBy(() -> new ExamDescription(null))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("시험 설명은 필수입니다.");
    }

    @Test
    @DisplayName("시험 설명은 2000자 이하여야 한다.")
    void descriptionLength() {
        // given
        String description = "a".repeat(2001);

        // when & then
        assertThatThrownBy(() -> new ExamDescription(description))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("시험 설명은 2000자 이하여야 합니다.");
    }
}
