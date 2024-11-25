package com.pass.unit.exam.command.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.pass.exam.domain.Exam;
import com.pass.exam.domain.Question;
import com.pass.exam.domain.QuestionGroup;
import com.pass.global.exception.BadRequestException;
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
                .mapToObj(i -> Question.shortAnswer("질문 %d".formatted(i + 1), "정답", Exam.initial("시험", 1L)))
                .toList();

        // when & then
        assertThatThrownBy(() -> new QuestionGroup(overSizedQuestions))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("질문은 1개 이상 200개 이하여야 합니다.");
    }
}
