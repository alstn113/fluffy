package com.pass.submission.command.domain;

import com.pass.global.persistence.AuditableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "submission")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Submission extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long formId;

    @Column(nullable = false)
    private Long memberId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "submission_id", nullable = false)
    private final List<Answer> answers = new ArrayList<>();

    public Submission(Long formId, Long memberId, List<Answer> answers) {
        this(null, formId, memberId, new ArrayList<>(answers));
    }

    public Submission(Long id, Long formId, Long memberId, List<Answer> answers) {
        this.id = id;
        this.formId = formId;
        this.memberId = memberId;
        this.answers.addAll(answers);
    }
}
