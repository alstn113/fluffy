package com.pass.exam.domain;

import com.pass.global.persistence.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Exam extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, columnDefinition = "VARCHAR(20)")
    @Enumerated(EnumType.STRING)
    private ExamStatus status;

    @Column(nullable = false)
    private Long memberId;

    @Embedded
    private final QuestionGroup questionGroup = new QuestionGroup();

    public static Exam initial(String title, Long memberId) {
        return new Exam(title, "", ExamStatus.DRAFT, memberId, new ArrayList<>());
    }

    public Exam(String title, String description, ExamStatus status, Long memberId, List<Question> questions) {
        this(null, title, description, status, memberId, questions);
    }

    public Exam(
            Long id,
            String title,
            String description,
            ExamStatus status,
            Long memberId,
            List<Question> questions
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.memberId = memberId;
        addQuestions(questions);
    }

    public boolean isNotWrittenBy(Long memberId) {
        return !this.memberId.equals(memberId);
    }

    public void addQuestions(List<Question> questions) {
        questions.forEach(question -> question.updateExam(this));
        questionGroup.addAll(new QuestionGroup(questions));
    }

    public void updateQuestionGroup(List<Question> questions) {
        questionGroup.clear();
        addQuestions(questions);
    }
}
