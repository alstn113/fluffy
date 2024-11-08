package com.pass.form.query.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "form")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
public class FormData {

    @Id
    private String id;

    private String title;

    private String description;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id")
    private final List<QuestionData> questions = new ArrayList<>();

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public FormData(String id, String title, String description, List<QuestionData> questions) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.questions.addAll(questions);
    }
}
