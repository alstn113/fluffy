package com.pass.exam.command.domain;

import com.pass.global.exception.BadRequestException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class QuestionGroup {

    private static final int MAX_QUESTION_SIZE = 200;

    @OneToMany(
            mappedBy = "exam",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private final List<Question> questions = new ArrayList<>();

    public static QuestionGroup empty() {
        return new QuestionGroup();
    }

    protected QuestionGroup() {
        this(new ArrayList<>());
    }

    public QuestionGroup(List<Question> questions) {
        validate(questions);

        this.questions.addAll(questions);
    }

    private void validate(List<Question> questions) {
        validateQuestionSize(questions);
    }

    private void validateQuestionSize(List<Question> questions) {
        if (questions.size() > MAX_QUESTION_SIZE) {
            throw new BadRequestException("질문은 1개 이상 %d개 이하여야 합니다.".formatted(MAX_QUESTION_SIZE));
        }
    }

    public List<Question> toList() {
        return new ArrayList<>(questions);
    }

    public void updateExam(Exam exam) {
        questions.forEach(question -> question.updateExam(exam));
    }

    public int size() {
        return questions.size();
    }

    public void clear() {
        for (Question question : toList()) {
            question.updateExam(null); // 관계 해제
        }
        questions.clear();
    }
}
