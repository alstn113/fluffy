package com.pass.exam.command.domain;

import com.pass.exam.command.domain.exception.DuplicateQuestionOptionException;
import com.pass.exam.command.domain.exception.InvalidQuestionOptionSizeException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Embeddable
public class QuestionOptionGroup {

    private static final int MAX_OPTION_SIZE = 10;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "question_id", nullable = false)
    private final List<QuestionOption> options;

    public static QuestionOptionGroup empty() {
        return new QuestionOptionGroup();
    }

    protected QuestionOptionGroup() {
        this(new ArrayList<>());
    }

    public QuestionOptionGroup(List<QuestionOption> options) {
        validate(options);

        IntStream.range(0, options.size())
                .forEach(index -> options.get(index).updateSequence(index + 1));

        this.options = new ArrayList<>(options);
    }

    private void validate(List<QuestionOption> options) {
        validateDuplicateOption(options);
        validateOptionSize(options);
    }

    private void validateDuplicateOption(List<QuestionOption> options) {
        List<String> optionNames = options.stream()
                .map(QuestionOption::getText)
                .toList();

        if (optionNames.stream().distinct().count() != optionNames.size()) {
            throw new DuplicateQuestionOptionException();
        }
    }

    private void validateOptionSize(List<QuestionOption> options) {
        if (options.size() > MAX_OPTION_SIZE) {
            throw new InvalidQuestionOptionSizeException(MAX_OPTION_SIZE);
        }
    }

    public List<QuestionOption> toList() {
        return new ArrayList<>(options);
    }
}
