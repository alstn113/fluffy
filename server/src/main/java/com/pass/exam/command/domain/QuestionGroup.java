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

    protected QuestionGroup() {
        this(new ArrayList<>());
    }

    public QuestionGroup(List<Question> questions) {
        validateQuestionSize(questions);

        this.questions.addAll(new ArrayList<>(questions));
    }

    private void validateQuestionSize(List<Question> questions) {
        if (questions.size() > MAX_QUESTION_SIZE) {
            throw new BadRequestException("질문은 1개 이상 %d개 이하여야 합니다.".formatted(MAX_QUESTION_SIZE));
        }
    }

    public void clear() {
        questions.clear();
    }

    public void addAll(QuestionGroup questionGroup) {
        questions.addAll(questionGroup.toList());
    }

    public List<Question> toList() {
        return new ArrayList<>(questions);
    }
}
