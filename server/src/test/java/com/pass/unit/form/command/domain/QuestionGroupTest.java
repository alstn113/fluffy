package com.pass.unit.form.command.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

import com.pass.form.command.domain.Form;
import com.pass.form.command.domain.Question;
import com.pass.form.command.domain.QuestionGroup;
import com.pass.form.command.domain.excetion.InvalidQuestionSizeException;
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
                .mapToObj(i -> Question.shortAnswer("질문 %d".formatted(i + 1), "정답", Form.initial("폼")))
                .toList();

        // when & then
        assertThatThrownBy(() -> new QuestionGroup(overSizedQuestions))
                .isInstanceOf(InvalidQuestionSizeException.class)
                .hasMessage("질문은 1~200개만 허용됩니다.");
    }

    @Test
    @DisplayName("질문 그룹 생성 시 순서대로 질문의 순서가 지정된다.")
    void assignQuestionSequenceWhenCreateQuestionGroup() {
        // given
        Form form = Form.initial("폼");
        List<Question> questions = List.of(
                Question.shortAnswer("질문 1", "정답", form),
                Question.shortAnswer("질문 2", "정답", form),
                Question.shortAnswer("질문 3", "정답", form)
        );

        // when
        QuestionGroup questionGroup = new QuestionGroup(questions);

        // then
        List<Question> actualQuestions = questionGroup.toList();
        assertThat(actualQuestions).extracting(Question::getSequence, Question::getText)
                .containsExactly(tuple(1, "질문 1"), tuple(2, "질문 2"), tuple(3, "질문 3"));
    }
}
