package com.pass.submission.command.domain;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "answer")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long questionId;

    @Column
    private String text;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "answer_choice", joinColumns = @JoinColumn(name = "answer_id"))
    private final List<Choice> choices = new ArrayList<>();

    public static Answer textAnswer(Long questionId, String text) {
        return new Answer(questionId, text, new ArrayList<>());
    }

    public static Answer choiceAnswer(Long questionId, List<Choice> choices) {
        return new Answer(questionId, "", choices);
    }

    public Answer(Long questionId, String text, List<Choice> choices) {
        this(null, questionId, text, new ArrayList<>(choices));
    }

    public Answer(Long id, Long questionId, String text, List<Choice> choices) {
        this.id = id;
        this.questionId = questionId;
        this.text = text;
        this.choices.addAll(choices);
    }
}
