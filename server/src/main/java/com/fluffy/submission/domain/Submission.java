package com.fluffy.submission.domain;

import com.fluffy.global.persistence.AuditableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Submission extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long examId;

    @Column(nullable = false)
    private Long memberId;

    @OneToMany(
            mappedBy = "submission",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private final List<Answer> answers = new ArrayList<>();

    public Submission(Long examId, Long memberId, List<Answer> answers) {
        this(null, examId, memberId, new ArrayList<>(answers));
    }

    public Submission(Long id, Long examId, Long memberId, List<Answer> answers) {
        this.id = id;
        this.examId = examId;
        this.memberId = memberId;
        addAnswers(answers);
    }

    private void addAnswers(List<Answer> answers) {
        answers.forEach(answer -> answer.updateSubmission(this));
        this.answers.addAll(answers);
    }

    public boolean isNotWrittenBy(Long memberId) {
        return !this.memberId.equals(memberId);
    }
}
