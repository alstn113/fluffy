package com.fluffy.exam.domain;

import com.fluffy.global.exception.BadRequestException;
import com.fluffy.global.persistence.AuditableEntity;
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

    @Embedded
    private ExamTitle title;

    @Embedded
    private ExamDescription description;

    @Column(nullable = false, columnDefinition = "VARCHAR(20)")
    @Enumerated(EnumType.STRING)
    private ExamStatus status;

    @Column(nullable = false)
    private Long memberId;

    @Embedded
    private final QuestionGroup questionGroup = new QuestionGroup();

    @Embedded
    private ExamPeriod examPeriod;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean isSingleAttempt = false;

    public static Exam create(String title, Long memberId) {
        Exam exam = new Exam();
        exam.title = new ExamTitle(title);
        exam.description = new ExamDescription("");
        exam.status = ExamStatus.DRAFT;
        exam.memberId = memberId;

        return exam;
    }

    public void publish(LocalDateTime startAt, LocalDateTime endAt) {
        if (status.isPublished()) {
            throw new BadRequestException("시험은 이미 출시되었습니다.");
        }

        if (questionGroup.size() < 1) {
            throw new BadRequestException("시험을 출시하기 위해서는 최소 1개 이상의 문제를 추가해야 합니다.");
        }

        this.examPeriod = ExamPeriod.create(startAt, endAt);
        this.status = ExamStatus.PUBLISHED;
    }

    public void updateQuestions(List<Question> questions) {
        if (status.isPublished()) {
            throw new BadRequestException("시험이 출시된 후에는 문제를 수정할 수 없습니다.");
        }

        questionGroup.clear();
        addQuestions(questions);
        update();
    }

    public boolean isNotWrittenBy(Long memberId) {
        return !this.memberId.equals(memberId);
    }

    public boolean isNotPublished() {
        return !status.isPublished();
    }

    private void addQuestions(List<Question> questions) {
        questions.forEach(question -> question.updateExam(this));
        questionGroup.addAll(new QuestionGroup(questions));
    }

    public void updateTitle(String title) {
        if (status.isPublished()) {
            throw new BadRequestException("시험이 출시된 후에는 정보를 수정할 수 없습니다.");
        }

        this.title = new ExamTitle(title);
    }

    public void updateDescription(String description) {
        if (status.isPublished()) {
            throw new BadRequestException("시험이 출시된 후에는 정보를 수정할 수 없습니다.");
        }

        this.description = new ExamDescription(description);
    }

    public String getTitle() {
        return title.getValue();
    }

    public String getDescription() {
        return description.getValue();
    }
}
