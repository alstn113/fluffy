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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "exam")
    private final List<Question> questions;

    public static QuestionGroup empty() {
        return new QuestionGroup();
    }

    protected QuestionGroup() {
        this(new ArrayList<>());
    }

    public QuestionGroup(List<Question> questions) {
        validate(questions);

        this.questions = new ArrayList<>(questions);
    }

    private void validate(List<Question> questions) {
        validateQuestionSize(questions);
    }

    private void validateQuestionSize(List<Question> questions) {
        if (questions.size() > MAX_QUESTION_SIZE) {
            throw new BadRequestException("질문은 최대 %d개까지만 등록할 수 있습니다.".formatted(MAX_QUESTION_SIZE));
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
}
