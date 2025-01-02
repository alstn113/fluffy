package com.fluffy.exam.domain;

import com.fluffy.global.exception.BadRequestException;
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

    @Column(nullable = false, columnDefinition = "VARCHAR(2000)")
    private String text;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String passage;

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

    public static Question shortAnswer(String text, String passage, String correctAnswer) {
        if (correctAnswer == null || correctAnswer.isBlank() || correctAnswer.length() > MAX_TEXT_LENGTH) {
            throw new BadRequestException("정답의 길이는 1자 이상 %d자 이하여야 합니다.".formatted(MAX_TEXT_LENGTH));
        }

        return answer(text, passage, QuestionType.SHORT_ANSWER, correctAnswer);
    }

    public static Question longAnswer(String text, String passage) {
        return answer(text, passage, QuestionType.LONG_ANSWER, null);
    }

    public static Question singleChoice(String text, String passage, List<QuestionOption> options) {
        if (options.stream().filter(QuestionOption::isCorrect).count() != 1) {
            throw new BadRequestException("객관식 단일 선택은 정답이 1개여야 합니다.");
        }

        return choice(text, passage, QuestionType.SINGLE_CHOICE, options);
    }

    public static Question multipleChoice(String text, String passage, List<QuestionOption> options) {
        return choice(text, passage, QuestionType.MULTIPLE_CHOICE, options);
    }

    public static Question trueOrFalse(String text, String passage, boolean trueOrFalse) {
        return choice(text, passage, QuestionType.TRUE_OR_FALSE, List.of(
                new QuestionOption("TRUE", trueOrFalse),
                new QuestionOption("FALSE", !trueOrFalse)
        ));
    }

    private static Question answer(String text, String passage, QuestionType type, String correctAnswer) {
        return new Question(text, passage, type, correctAnswer, new ArrayList<>());
    }

    private static Question choice(String text, String passage, QuestionType type, List<QuestionOption> options) {
        return new Question(text, passage, type, null, options);
    }

    private Question(
            String text,
            String passage,
            QuestionType type,
            String correctAnswer,
            List<QuestionOption> options
    ) {
        this(null, text, passage, type, correctAnswer, options);
    }

    private Question(
            Long id,
            String text,
            String passage,
            QuestionType type,
            String correctAnswer,
            List<QuestionOption> options
    ) {
        validate(text);

        this.id = id;
        this.text = text;
        this.passage = passage;
        this.type = type;
        this.correctAnswer = correctAnswer;
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
