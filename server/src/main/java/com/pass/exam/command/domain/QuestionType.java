package com.pass.exam.command.domain;

import com.pass.exam.command.domain.exception.QuestionTypeNotFoundException;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum QuestionType {

    SHORT_ANSWER(true),
    LONG_ANSWER(false),
    SINGLE_CHOICE(true),
    MULTIPLE_CHOICE(true),
    TRUE_OR_FALSE(true);

    private final boolean autoGraded;

    QuestionType(boolean autoGraded) {
        this.autoGraded = autoGraded;
    }

    public static QuestionType from(String value) {
        return Arrays.stream(values())
                .filter(it -> it.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(QuestionTypeNotFoundException::new);
    }

    public boolean isTextAnswerable() {
        return this == SHORT_ANSWER || this == LONG_ANSWER;
    }
}
