package com.pass.exam.domain;

import com.pass.global.exception.BadRequestException;
import com.pass.global.persistence.AuditableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
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

    @Embedded
    private ExamPeriod examPeriod;

    public static Exam create(String title, Long memberId) {
        Exam exam = new Exam();
        exam.title = title;
        exam.description = "";
        exam.status = ExamStatus.DRAFT;
        exam.memberId = memberId;

        return exam;
    }

    public void publish(LocalDateTime startedAt, LocalDateTime endedAt) {
        if (status.isNotPublishable()) {
            throw new BadRequestException("시험을 출시할 수 없는 상태입니다.");
        }

        if (questionGroup.size() < 1) {
            throw new BadRequestException("시험을 출시하기 위해서는 최소 1개 이상의 문제를 추가해야 합니다.");
        }

        this.examPeriod = ExamPeriod.create(startedAt, endedAt);
        this.status = ExamStatus.PUBLISHED;
    }

    public void updateQuestions(List<Question> questions) {
        if (status.isNotEditable()) {
            throw new BadRequestException("시험이 시작된 후에는 문제를 수정할 수 없습니다.");
        }

        questionGroup.clear();
        addQuestions(questions);
        update();
    }

    public boolean isNotWrittenBy(Long memberId) {
        return !this.memberId.equals(memberId);
    }

    private void addQuestions(List<Question> questions) {
        questions.forEach(question -> question.updateExam(this));
        questionGroup.addAll(new QuestionGroup(questions));
    }
}
