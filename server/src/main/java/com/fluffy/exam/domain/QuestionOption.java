package com.fluffy.exam.domain;

import com.fluffy.global.exception.BadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

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
            throw new BadRequestException("질문 옵션의 길이는 1~%d자 이어야 합니다.".formatted(MAX_TEXT_LENGTH));
        }
    }

    public void updateQuestion(Question question) {
        this.question = question;
    }
}
