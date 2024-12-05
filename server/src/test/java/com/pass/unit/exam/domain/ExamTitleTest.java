package com.pass.unit.exam.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.pass.exam.domain.ExamTitle;
import com.pass.global.exception.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ExamTitleTest {

    @Test
    @DisplayName("시험 제목은 생성할 수 있다.")
    void create() {
        // given
        String title = "a".repeat(100);

        // when
        ExamTitle examTitle = new ExamTitle(title);

        // then
        assertThat(examTitle.getValue()).isEqualTo(title);
    }

    @Test
    @DisplayName("시험 제목은 비어있을 수 없다.")
    void titleNotNullAndNotBlank() {
        assertThatThrownBy(() -> new ExamTitle(null))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("시험 제목은 비어있을 수 없습니다.");
        assertThatThrownBy(() -> new ExamTitle(" "))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("시험 제목은 비어있을 수 없습니다.");
    }

    @Test
    @DisplayName("시험 제목은 100자 이하여야 한다.")
    void titleLength() {
        // given
        String title = "a".repeat(101);

        // when & then
        assertThatThrownBy(() -> new ExamTitle(title))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("시험 제목은 100자 이하여야 합니다.");
    }
}
