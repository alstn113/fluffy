package com.pass.exam.command.domain;

import com.pass.global.persistence.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "exam")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Exam extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Long memberId;

    @Embedded
    private QuestionGroup questionGroup;

    public static Exam initial(String title, Long memberId) {
        return new Exam(title, "", memberId, QuestionGroup.empty());
    }

    public Exam(String title, String description, Long memberId, QuestionGroup questionGroup) {
        this(null, title, description, memberId, questionGroup);
    }

    public Exam(String id, String title, String description, Long memberId, QuestionGroup questionGroup) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.memberId = memberId;
        this.questionGroup = questionGroup;
    }

    public boolean isNotWrittenBy(Long memberId) {
        return !this.memberId.equals(memberId);
    }

    public void addQuestions(QuestionGroup questionGroup, Exam exam) {
        this.questionGroup = questionGroup;
        questionGroup.updateExam(exam);
    }
}
