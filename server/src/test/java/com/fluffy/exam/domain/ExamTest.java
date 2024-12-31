package com.fluffy.exam.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.fluffy.global.exception.BadRequestException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ExamTest {

    @Test
    @DisplayName("시험을 생성할 수 있다.")
    void create() {
        // given
        String title = "시험 제목";
        Long memberId = 1L;

        // when
        Exam exam = Exam.create(title, memberId);

        // then
        assertAll(
                () -> assertThat(exam.getTitle()).isEqualTo(title),
                () -> assertThat(exam.getMemberId()).isEqualTo(memberId),
                () -> assertThat(exam.getDescription()).isEmpty(),
                () -> assertThat(exam.getStatus()).isEqualTo(ExamStatus.DRAFT),
                () -> assertThat(exam.isSingleAttempt()).isFalse()
        );
    }

    @Test
    @DisplayName("시험 문제들을 업데이트할 경우 기존 문제들은 삭제되고 새로운 문제들로 대체된다.")
    void updateQuestions() {
        // given
        Exam exam = Exam.create("시험 제목", 1L);
        exam.updateQuestions(List.of(
                Question.shortAnswer("단답형1", "답1"),
                Question.trueOrFalse("O/X1", true)
        ));

        List<Question> prevQuestions = exam.getQuestionGroup().toList();
        assertThat(prevQuestions).hasSize(2);

        // when
        exam.updateQuestions(List.of(
                Question.multipleChoice("객관식 복수", List.of(
                                new QuestionOption("선택1", true),
                                new QuestionOption("선택2", false),
                                new QuestionOption("선택3", false)
                        )
                ))
        );

        // then
        List<Question> updatedQuestions = exam.getQuestionGroup().toList();
        assertThat(updatedQuestions).hasSize(1);
        assertThat(updatedQuestions.getFirst().getText()).isEqualTo("객관식 복수");
        assertThat(updatedQuestions.getFirst().getOptionGroup().toList()).hasSize(3);
    }

    @Test
    @DisplayName("시험을 출시할 수 있다.")
    void publish() {
        // given
        Exam exam = Exam.create("시험 제목", 1L);
        exam.updateQuestions(List.of(
                Question.shortAnswer("단답형1", "답1"),
                Question.trueOrFalse("O/X1", true)
        ));

        // when
        exam.publish(null, null);

        // then
        assertAll(
                () -> assertThat(exam.getStatus()).isEqualTo(ExamStatus.PUBLISHED),
                () -> assertThat(exam.getExamPeriod().getStartAt()).isNotNull()
        );
    }

    @Test
    @DisplayName("시험을 출시된 이후 또 다시 출시할 수 없다.")
    void publishTwice() {
        // given
        Exam exam = Exam.create("시험 제목", 1L);
        exam.updateQuestions(List.of(
                Question.shortAnswer("단답형1", "답1"),
                Question.trueOrFalse("O/X1", true)
        ));
        exam.publish(null, null);

        // when & then
        assertThatThrownBy(() -> exam.publish(null, null))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("시험은 이미 출시되었습니다.");
    }

    @Test
    @DisplayName("시험을 출시하기 위해서는 최소 1개 이상의 문제를 추가해야 한다.")
    void publishWithoutQuestions() {
        // given
        Exam exam = Exam.create("시험 제목", 1L);

        // when & then
        assertThatThrownBy(() -> exam.publish(null, null))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("시험을 출시하기 위해서는 최소 1개 이상의 문제를 추가해야 합니다.");
    }

    @Test
    @DisplayName("시험이 출시된 이후에는 문제를 수정할 수 없다.")
    void updateQuestionsAfterPublish() {
        // given
        Exam exam = Exam.create("시험 제목", 1L);
        exam.updateQuestions(List.of(
                Question.shortAnswer("단답형1", "답1"),
                Question.trueOrFalse("O/X1", true)
        ));
        exam.publish(null, null);

        // when & then
        List<Question> questions = List.of(Question.shortAnswer("단답형2", "답2"));
        assertThatThrownBy(() -> exam.updateQuestions(questions))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("시험이 출시된 후에는 문제를 수정할 수 없습니다.");
    }

    @Test
    @DisplayName("시험 제목을 수정할 수 있다.")
    void updateTitle() {
        // given
        Exam exam = Exam.create("시험 제목", 1L);

        // when
        exam.updateTitle("수정된 시험 제목");

        // then
        assertThat(exam.getTitle()).isEqualTo("수정된 시험 제목");
    }

    @Test
    @DisplayName("시험 제목을 출시된 이후에는 수정할 수 없다.")
    void updateTitleAfterPublish() {
        // given
        Exam exam = Exam.create("시험 제목", 1L);
        exam.updateQuestions(List.of(
                Question.shortAnswer("단답형1", "답1"),
                Question.trueOrFalse("O/X1", true)
        ));
        exam.publish(null, null);

        // when & then
        assertThatThrownBy(() -> exam.updateTitle("수정된 시험 제목"))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("시험이 출시된 후에는 정보를 수정할 수 없습니다.");
    }

    @Test
    @DisplayName("시험 설명을 수정할 수 있다.")
    void updateDescription() {
        // given
        Exam exam = Exam.create("시험 제목", 1L);

        // when
        exam.updateDescription("시험 설명");

        // then
        assertThat(exam.getDescription()).isEqualTo("시험 설명");
    }

    @Test
    @DisplayName("시험 설명을 출시된 이후에는 수정할 수 없다.")
    void updateDescriptionAfterPublish() {
        // given
        Exam exam = Exam.create("시험 제목", 1L);
        exam.updateQuestions(List.of(
                Question.shortAnswer("단답형1", "답1"),
                Question.trueOrFalse("O/X1", true)
        ));
        exam.publish(null, null);

        // when & then
        assertThatThrownBy(() -> exam.updateDescription("시험 설명"))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("시험이 출시된 후에는 정보를 수정할 수 없습니다.");
    }
}
