package com.pass.exam.command.domain;

import com.pass.exam.command.domain.exception.QuestionTypeNotFoundException;
import java.util.Arrays;

public enum QuestionType {

    SHORT_ANSWER,
    LONG_ANSWER,
    SINGLE_CHOICE,
    MULTIPLE_CHOICE,
    TRUE_OR_FALSE;

    public static QuestionType from(String value) {
        return Arrays.stream(values())
                .filter(it -> it.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(QuestionTypeNotFoundException::new);
    }
}
