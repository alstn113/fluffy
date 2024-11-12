package com.pass.exam.command.domain;

import com.pass.exam.command.domain.exception.ExamStatusNotFoundException;
import java.util.Arrays;

public enum ExamStatus {

    DRAFT,
    PUBLISHED,
    ;

    public static ExamStatus from(String value) {
        return Arrays.stream(values())
                .filter(it -> it.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new ExamStatusNotFoundException(value));
    }
}
