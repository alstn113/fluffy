package com.fluffy.exam.domain;

import com.fluffy.infra.persistence.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ExamImage extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private Long memberId;

    @Column(nullable = false)
    private Long examId;

    @Column(nullable = false)
    private String path;

    @Column(nullable = false)
    private Long fileSize;

    public ExamImage(Long memberId, Long examId, String path, Long fileSize) {
        this(null, memberId, examId, path, fileSize);
    }

    public ExamImage(UUID id, Long memberId, Long examId, String path, Long fileSize) {
        this.id = id;
        this.memberId = memberId;
        this.examId = examId;
        this.path = path;
        this.fileSize = fileSize;
    }
}
