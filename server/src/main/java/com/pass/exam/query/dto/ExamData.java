package com.pass.exam.query.dto;

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
@Table(name = "exam")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
public class ExamData {

    @Id
    private String id;

    private String title;

    private String description;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id")
    private final List<QuestionData> questions = new ArrayList<>();

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public ExamData(String id, String title, String description, List<QuestionData> questions) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.questions.addAll(questions);
    }
}
