package com.pass.unit.exam.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.pass.exam.domain.Exam;
import com.pass.exam.domain.Question;
import com.pass.exam.domain.QuestionOption;
import com.pass.exam.domain.QuestionType;
import com.pass.global.exception.BadRequestException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class QuestionTest {

    @ParameterizedTest
    @ValueSource(strings = {"", " "})
    @DisplayName("질문의 길이는 공백일 수 없습니다.")
    void failWhenTextIsBlank(String text) {
        // when & then
        Exam exam = Exam.create("시험 제목", 1L);
        assertThatThrownBy(() -> Question.shortAnswer(text, "정답", exam))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("질문의 길이는 1자 이상 2000자 이하여야 합니다.");
    }

    @Test
    @DisplayName("질문의 길이는 2000자를 초과할 수 없습니다.")
    void failWhenTextLengthIsInvalid() {
        // given
        int length = 2001;

        // when
        String text = "a".repeat(length);

        // then
        assertThatThrownBy(() -> Question.shortAnswer(text, "정답", Exam.create("시험 제목", 1L)))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("질문의 길이는 1자 이상 2000자 이하여야 합니다.");
    }

    @Test
    @DisplayName("주관식 정답의 길이는 2000자를 초과할 수 없습니다.")
    void failWhenShortAnswerCorrectAnswerLengthIsInvalid() {
        // given
        int length = 2001;

        // when
        String correctAnswer = "a".repeat(length);

        // then
        assertThatThrownBy(() -> Question.shortAnswer("질문", correctAnswer, Exam.create("시험 제목", 1L)))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("정답의 길이는 1자 이상 2000자 이하여야 합니다.");
    }

    @Test
    @DisplayName("객관식 단일 선택은 정답이 1개여야 합니다.")
    void failWhenSingleChoiceOptionsHasMultipleCorrectAnswers() {
        // given
        QuestionOption correctOption = new QuestionOption("정답1", true);
        QuestionOption incorrectOption = new QuestionOption("정답2", true);
        List<QuestionOption> options = List.of(correctOption, incorrectOption);

        // when & then
        Exam exam = Exam.create("시험 제목", 1L);
        assertThatThrownBy(() -> Question.singleChoice("질문", exam, options))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("객관식 단일 선택은 정답이 1개여야 합니다.");
    }

    @Test
    @DisplayName("True or False 질문을 생성할 수 있다")
    void trueOrFalse() {
        // given
        Question question = Question.trueOrFalse("질문", Exam.create("시험 제목", 1L), false);

        // then
        List<QuestionOption> options = question.getOptionGroup().toList();
        assertAll(
                () -> assertThat(question.getText()).isEqualTo("질문"),
                () -> assertThat(question.getType()).isEqualTo(QuestionType.TRUE_OR_FALSE),
                () -> assertThat(options).extracting(QuestionOption::getText, QuestionOption::isCorrect)
                        .containsExactly(tuple("TRUE", false), tuple("FALSE", true))
        );
    }
}