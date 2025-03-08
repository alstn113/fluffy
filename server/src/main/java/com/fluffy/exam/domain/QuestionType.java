package com.fluffy.exam.domain;

import com.fluffy.global.exception.NotFoundException;
import java.util.Arrays;
import lombok.Getter;

@Getter
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
                .orElseThrow(() -> new NotFoundException("존재하지 않는 질문 유형입니다. 질문 유형: " + value));
    }
}
