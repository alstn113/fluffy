package com.pass.form.command.domain;

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
@Table(name = "form")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Form extends AuditableEntity {

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

    public static Form initial(String title, Long memberId) {
        return new Form(title, "", memberId, QuestionGroup.empty());
    }

    public Form(String title, String description, Long memberId, QuestionGroup questionGroup) {
        this(null, title, description, memberId, questionGroup);
    }

    public Form(String id, String title, String description, Long memberId, QuestionGroup questionGroup) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.memberId = memberId;
        this.questionGroup = questionGroup;
    }

    public boolean isNotWrittenBy(Long memberId) {
        return !this.memberId.equals(memberId);
    }

    public void addQuestions(QuestionGroup questionGroup, Form form) {
        this.questionGroup = questionGroup;
        questionGroup.updateForm(form);
    }
}
