package com.pass.form.command.domain;

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
import jakarta.persistence.Table;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "question")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Question {

    private static final int START_SEQUENCE = 1;
    private static final int MAX_TEXT_LENGTH = 2000;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private int sequence;

    @Column(nullable = false, columnDefinition = "VARCHAR(50)")
    @Enumerated(EnumType.STRING)
    private QuestionType type;

    @Column
    private String correctAnswer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id")
    private Form form;

    @Embedded
    private QuestionOptionGroup optionGroup;

    public static Question shortAnswer(String text, String correctAnswer, Form form) {
        if (correctAnswer == null || correctAnswer.isBlank() || correctAnswer.length() > MAX_TEXT_LENGTH) {
            String message = "정답의 길이는 1~%d자 이어야 합니다.".formatted(MAX_TEXT_LENGTH);
            throw new IllegalArgumentException(message);
        }

        return answer(text, QuestionType.SHORT_ANSWER, correctAnswer, form);
    }

    public static Question longAnswer(String text, Form form) {
        return answer(text, QuestionType.LONG_ANSWER, null, form);
    }

    public static Question singleChoice(String text, Form form, List<QuestionOption> options) {
        if (options.stream().filter(QuestionOption::isCorrect).count() != 1) {
            throw new IllegalArgumentException("객관식 단일 선택은 정답이 1개여야 합니다.");
        }

        return choice(text, QuestionType.SINGLE_CHOICE, form, options);
    }

    public static Question multipleChoice(String text, Form form, List<QuestionOption> options) {
        return choice(text, QuestionType.MULTIPLE_CHOICE, form, options);
    }

    public static Question trueOrFalse(String text, Form form, String trueText, String falseText, boolean trueOrFalse) {
        return choice(text, QuestionType.TRUE_OR_FALSE, form, List.of(
                new QuestionOption(trueText, trueOrFalse),
                new QuestionOption(falseText, !trueOrFalse)
        ));
    }

    private static Question answer(String text, QuestionType type, String correctAnswer, Form form) {
        return new Question(text, START_SEQUENCE, type, correctAnswer, form, QuestionOptionGroup.empty());
    }

    private static Question choice(String text, QuestionType type, Form form, List<QuestionOption> options) {
        return new Question(text, START_SEQUENCE, type, null, form, new QuestionOptionGroup(options));
    }

    public Question(
            String text,
            int sequence,
            QuestionType type,
            String correctAnswer,
            Form form,
            QuestionOptionGroup optionGroup
    ) {
        this(null, text, sequence, type, correctAnswer, form, optionGroup);
    }

    public Question(
            Long id,
            String text,
            int sequence,
            QuestionType type,
            String correctAnswer,
            Form form,
            QuestionOptionGroup optionGroup
    ) {
        validate(text);

        this.id = id;
        this.text = text;
        this.sequence = sequence;
        this.type = type;
        this.correctAnswer = correctAnswer;
        this.form = form;
        this.optionGroup = optionGroup;
    }

    private void validate(String text) {
        validateTextLength(text);
    }

    private void validateTextLength(String text) {
        if (text.isBlank() || text.length() > MAX_TEXT_LENGTH) {
            String message = "질문의 길이는 1~%d자 이어야 합니다.".formatted(MAX_TEXT_LENGTH);
            throw new IllegalArgumentException(message);
        }
    }

    public void updateSequence(int sequence) {
        this.sequence = sequence;
    }

    public void updateForm(Form form) {
        this.form = form;
    }
}
