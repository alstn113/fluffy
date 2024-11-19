package com.pass.exam.query.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "question_option")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class QuestionOptionData {

    @Id
    private Long id;

    private String text;

    private boolean isCorrect;

    public QuestionOptionData(Long id, String text, boolean isCorrect) {
        this.id = id;
        this.text = text;
        this.isCorrect = isCorrect;
    }
}
