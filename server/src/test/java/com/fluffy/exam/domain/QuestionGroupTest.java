package com.fluffy.exam.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fluffy.global.exception.BadRequestException;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class QuestionGroupTest {

    @Test
    @DisplayName("질문 그룹 생성 시 질문이 200개를 초과할 수 없다.")
    void failWhenQuestionSizeIsOver() {
        // given
        int questionSize = 201;
        List<Question> overSizedQuestions = IntStream.range(0, questionSize)
                .mapToObj(i -> Question.shortAnswer("질문 %d".formatted(i + 1), "지문", "정답"))
                .toList();

        // when & then
        assertThatThrownBy(() -> new QuestionGroup(overSizedQuestions))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("질문은 1개 이상 200개 이하여야 합니다.");
    }
}
