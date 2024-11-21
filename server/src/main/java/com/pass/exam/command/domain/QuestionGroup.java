package com.pass.exam.command.domain;

import com.pass.exam.command.domain.exception.InvalidQuestionSizeException;
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
            throw new InvalidQuestionSizeException(MAX_QUESTION_SIZE);
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
