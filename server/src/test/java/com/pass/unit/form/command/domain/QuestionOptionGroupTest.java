package com.pass.unit.form.command.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

import com.pass.form.command.domain.QuestionOption;
import com.pass.form.command.domain.QuestionOptionGroup;
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
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("중복된 옵션은 허용되지 않습니다.");
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
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("옵션은 10개 이하만 허용됩니다.");
    }

    @Test
    @DisplayName("옵션 그룹 생성 시 순서대로 옵션의 순서가 지정된다.")
    void assignOptionSequenceWhenCreateOptionGroup() {
        // given
        List<QuestionOption> options = List.of(
                new QuestionOption("옵션 1", true),
                new QuestionOption("옵션 2", false),
                new QuestionOption("옵션 3", false)
        );

        // when
        QuestionOptionGroup optionGroup = new QuestionOptionGroup(options);

        // then
        List<QuestionOption> actualOptions = optionGroup.toList();
        assertThat(actualOptions).extracting(QuestionOption::getSequence, QuestionOption::getText)
                .containsExactly(tuple(1, "옵션 1"), tuple(2, "옵션 2"), tuple(3, "옵션 3"));
    }
}
