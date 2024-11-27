package com.pass.exam.domain;

import com.pass.global.exception.BadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Question {

    private static final int MAX_TEXT_LENGTH = 2000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false, columnDefinition = "VARCHAR(50)")
    @Enumerated(EnumType.STRING)
    private QuestionType type;

    @Column
    private String correctAnswer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id")
    private Exam exam;

    @Embedded
    private final QuestionOptionGroup optionGroup = new QuestionOptionGroup();

    public static Question shortAnswer(String text, String correctAnswer, Exam exam) {
        if (correctAnswer == null || correctAnswer.isBlank() || correctAnswer.length() > MAX_TEXT_LENGTH) {
            throw new BadRequestException("정답의 길이는 1자 이상 %d자 이하여야 합니다.".formatted(MAX_TEXT_LENGTH));
        }

        return answer(text, QuestionType.SHORT_ANSWER, correctAnswer, exam);
    }

    public static Question longAnswer(String text, Exam exam) {
        return answer(text, QuestionType.LONG_ANSWER, null, exam);
    }

    public static Question singleChoice(String text, Exam exam, List<QuestionOption> options) {
        if (options.stream().filter(QuestionOption::isCorrect).count() != 1) {
            throw new BadRequestException("객관식 단일 선택은 정답이 1개여야 합니다.");
        }

        return choice(text, QuestionType.SINGLE_CHOICE, exam, options);
    }

    public static Question multipleChoice(String text, Exam exam, List<QuestionOption> options) {
        return choice(text, QuestionType.MULTIPLE_CHOICE, exam, options);
    }

    public static Question trueOrFalse(String text, Exam exam, boolean trueOrFalse) {
        return choice(text, QuestionType.TRUE_OR_FALSE, exam, List.of(
                new QuestionOption("TRUE", trueOrFalse),
                new QuestionOption("FALSE", !trueOrFalse)
        ));
    }

    private static Question answer(String text, QuestionType type, String correctAnswer, Exam exam) {
        return new Question(text, type, correctAnswer, exam, new ArrayList<>());
    }

    private static Question choice(String text, QuestionType type, Exam exam, List<QuestionOption> options) {
        return new Question(text, type, null, exam, options);
    }

    public Question(
            String text,
            QuestionType type,
            String correctAnswer,
            Exam exam,
            List<QuestionOption> options
    ) {
        this(null, text, type, correctAnswer, exam, options);
    }

    public Question(
            Long id,
            String text,
            QuestionType type,
            String correctAnswer,
            Exam exam,
            List<QuestionOption> options
    ) {
        validate(text);

        this.id = id;
        this.text = text;
        this.type = type;
        this.correctAnswer = correctAnswer;
        this.exam = exam;
        addOptions(options);
    }

    private void validate(String text) {
        validateTextLength(text);
    }

    private void validateTextLength(String text) {
        if (text.isBlank() || text.length() > MAX_TEXT_LENGTH) {
            throw new BadRequestException("질문의 길이는 1자 이상 %d자 이하여야 합니다.".formatted(MAX_TEXT_LENGTH));
        }
    }

    public void updateExam(Exam exam) {
        this.exam = exam;
    }

    private void addOptions(List<QuestionOption> options) {
        options.forEach(option -> option.updateQuestion(this));
        optionGroup.addAll(new QuestionOptionGroup(options));
    }
}
