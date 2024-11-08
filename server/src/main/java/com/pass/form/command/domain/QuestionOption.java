package com.pass.form.command.domain;

import com.pass.form.command.domain.excetion.InvalidQuestionOptionLengthException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "question_option")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class QuestionOption {

    private static final int START_SEQUENCE = 1;
    private static final int MAX_TEXT_LENGTH = 200;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private int sequence;

    @Column(nullable = false)
    private boolean isCorrect;

    public QuestionOption(String text, boolean isCorrect) {
        this(null, text, START_SEQUENCE, isCorrect);
    }

    public QuestionOption(Long id, String text, int sequence, boolean isCorrect) {
        validate(text);

        this.id = id;
        this.text = text;
        this.sequence = sequence;
        this.isCorrect = isCorrect;
    }

    public void validate(String text) {
        validateTextLength(text);
    }

    public void validateTextLength(String text) {
        if (text.isBlank() || text.length() > MAX_TEXT_LENGTH) {
            throw new InvalidQuestionOptionLengthException(MAX_TEXT_LENGTH);
        }
    }

    public void updateSequence(int sequence) {
        this.sequence = sequence;
    }
}
