package com.pass.exam.query.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "question")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
public class QuestionData {

    @Id
    private Long id;

    private String text;

    private String type;

    private String correctAnswer;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private List<QuestionOptionData> options;

    public QuestionData(Long id, String text, String type, List<QuestionOptionData> options) {
        this.id = id;
        this.text = text;
        this.type = type;
        this.options = options;
    }
}
