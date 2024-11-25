package com.pass.unit.exam.command.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.pass.exam.domain.QuestionOption;
import com.pass.exam.domain.QuestionOptionGroup;
import com.pass.global.exception.BadRequestException;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class QuestionOptionGroupTest {

    @Test
    @DisplayName("옵션 텍스트는 중복될 수 없다.")
    void failWhenOptionTextIsDuplicated() {
        // given
        String optionName = "옵션 1";
        List<QuestionOption> options = List.of(
                new QuestionOption(optionName, true),
                new QuestionOption(optionName, false)
        );

        // when & then
        assertThatThrownBy(() -> new QuestionOptionGroup(options))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("중복된 질문 옵션은 허용되지 않습니다.");
    }

    @Test
    @DisplayName("옵션은 10개를 초과할 수 없다.")
    void failWhenOptionSizeIsInvalid() {
        // given
        int optionSize = 11;
        List<QuestionOption> options = IntStream.range(0, optionSize)
                .mapToObj(i -> new QuestionOption("옵션 " + i, true))
                .toList();

        // when & then
        assertThatThrownBy(() -> new QuestionOptionGroup(options))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("질문 옵션은 1~10개만 허용됩니다.");
    }
}
