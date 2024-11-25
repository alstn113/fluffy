package com.pass.exam.domain;

import com.pass.global.exception.NotFoundException;
import java.util.Arrays;

public enum ExamStatus {

    DRAFT,
    PUBLISHED,
    ;

    public static ExamStatus from(String value) {
        return Arrays.stream(values())
                .filter(it -> it.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("존재하지 않는 시험 상태입니다. 시험 상태: " + value));
    }
}
