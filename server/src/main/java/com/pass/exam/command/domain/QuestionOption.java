package com.pass.exam.command.domain;

import com.pass.exam.command.domain.exception.InvalidQuestionOptionLengthException;
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

    private static final int MAX_TEXT_LENGTH = 200;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private boolean isCorrect;

    public QuestionOption(String text, boolean isCorrect) {
        this(null, text, isCorrect);
    }

    public QuestionOption(Long id, String text, boolean isCorrect) {
        validate(text);

        this.id = id;
        this.text = text;
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
}
