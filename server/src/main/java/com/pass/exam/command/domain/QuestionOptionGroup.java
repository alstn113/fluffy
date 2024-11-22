package com.pass.exam.command.domain;

import com.pass.global.exception.BadRequestException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class QuestionOptionGroup {

    private static final int MAX_OPTION_SIZE = 10;

    @OneToMany(
            mappedBy = "question",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private final List<QuestionOption> options;

    protected QuestionOptionGroup() {
        this(new ArrayList<>());
    }

    public QuestionOptionGroup(List<QuestionOption> options) {
        validate(options);

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
            throw new BadRequestException("중복된 질문 옵션은 허용되지 않습니다.");
        }
    }

    private void validateOptionSize(List<QuestionOption> options) {
        if (options.size() > MAX_OPTION_SIZE) {
            throw new BadRequestException("질문 옵션은 1~%d개만 허용됩니다.".formatted(MAX_OPTION_SIZE));
        }
    }

    public void addAll(QuestionOptionGroup questionOptionGroup) {
        options.addAll(questionOptionGroup.toList());
    }

    public List<QuestionOption> toList() {
        return new ArrayList<>(options);
    }
}
