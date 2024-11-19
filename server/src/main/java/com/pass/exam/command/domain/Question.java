package com.pass.exam.command.domain;

import com.pass.exam.command.domain.exception.InvalidCorrectAnswerLengthException;
import com.pass.exam.command.domain.exception.InvalidQuestionLengthException;
import com.pass.exam.command.domain.exception.InvalidSingleChoiceCorrectAnswerSizeException;
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
    private QuestionOptionGroup optionGroup;

    public static Question shortAnswer(String text, String correctAnswer, Exam exam) {
        if (correctAnswer == null || correctAnswer.isBlank() || correctAnswer.length() > MAX_TEXT_LENGTH) {
            throw new InvalidCorrectAnswerLengthException(MAX_TEXT_LENGTH);
        }

        return answer(text, QuestionType.SHORT_ANSWER, correctAnswer, exam);
    }

    public static Question longAnswer(String text, Exam exam) {
        return answer(text, QuestionType.LONG_ANSWER, null, exam);
    }

    public static Question singleChoice(String text, Exam exam, List<QuestionOption> options) {
        if (options.stream().filter(QuestionOption::isCorrect).count() != 1) {
            throw new InvalidSingleChoiceCorrectAnswerSizeException();
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
        return new Question(text, type, correctAnswer, exam, QuestionOptionGroup.empty());
    }

    private static Question choice(String text, QuestionType type, Exam exam, List<QuestionOption> options) {
        return new Question(text, type, null, exam, new QuestionOptionGroup(options));
    }

    public Question(
            String text,
            QuestionType type,
            String correctAnswer,
            Exam exam,
            QuestionOptionGroup optionGroup
    ) {
        this(null, text, type, correctAnswer, exam, optionGroup);
    }

    public Question(
            Long id,
            String text,
            QuestionType type,
            String correctAnswer,
            Exam exam,
            QuestionOptionGroup optionGroup
    ) {
        validate(text);

        this.id = id;
        this.text = text;
        this.type = type;
        this.correctAnswer = correctAnswer;
        this.exam = exam;
        this.optionGroup = optionGroup;
    }

    private void validate(String text) {
        validateTextLength(text);
    }

    private void validateTextLength(String text) {
        if (text.isBlank() || text.length() > MAX_TEXT_LENGTH) {
            throw new InvalidQuestionLengthException(MAX_TEXT_LENGTH);
        }
    }

    public void updateExam(Exam exam) {
        this.exam = exam;
    }
}
