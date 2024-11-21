package com.pass.exam.command.domain;

import com.pass.global.persistence.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    private QuestionGroup questionGroup;

    public static Exam initial(String title, Long memberId) {
        return new Exam(title, "", ExamStatus.DRAFT, memberId, QuestionGroup.empty());
    }

    public Exam(String title, String description, ExamStatus status, Long memberId, QuestionGroup questionGroup) {
        this(null, title, description, status, memberId, questionGroup);
    }

    public Exam(
            Long id,
            String title,
            String description,
            ExamStatus status,
            Long memberId,
            QuestionGroup questionGroup
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.memberId = memberId;
        this.questionGroup = questionGroup;
    }

    public boolean isNotWrittenBy(Long memberId) {
        return !this.memberId.equals(memberId);
    }

    public void updateQuestionGroup(QuestionGroup questionGroup, Exam exam) {
        this.questionGroup = questionGroup;
        questionGroup.updateExam(exam);
    }

    public void clearQuestionGroup() {
        this.questionGroup.clear();
    }
}
