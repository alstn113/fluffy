package com.pass.exam.command.domain;

import com.pass.global.exception.NotFoundException;
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
                .orElseThrow(() -> new NotFoundException("존재하지 않는 질문 유형입니다. 질문 유형: " + value));
    }

    public boolean isTextAnswerable() {
        return this == SHORT_ANSWER || this == LONG_ANSWER;
    }
}
