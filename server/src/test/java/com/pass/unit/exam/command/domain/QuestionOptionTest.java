package com.pass.unit.exam.command.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.pass.exam.command.domain.QuestionOption;
import com.pass.global.exception.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class QuestionOptionTest {

    @Test
    @DisplayName("질문 옵션을 생성할 수 있다.")
    void create() {
        // when & then
        assertThatCode(() -> new QuestionOption("옵션 1", true))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    @DisplayName("옵션 텍스트가 공백일 경우 예외를 발생시킨다.")
    void failWhenTextIsBlank(String text) {
        // when & then
        assertThatCode(() -> new QuestionOption(text, true))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("질문 옵션의 길이는 1~200자 이어야 합니다.");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 201})
    @DisplayName("옵션 텍스트의 길이가 1~200자가 아닌 경우 경우 예외를 발생시킨다.")
    void failWhenTextLengthIsInvalid(int length) {
        // when
        String text = "a".repeat(length);

        // then
        assertThatThrownBy(() -> new QuestionOption(text, true))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("질문 옵션의 길이는 1~200자 이어야 합니다.");
    }
}
