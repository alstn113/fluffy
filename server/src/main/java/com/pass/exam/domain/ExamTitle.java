package com.pass.exam.domain;

import com.pass.global.exception.BadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ExamTitle {

    private static final int MAX_TITLE_LENGTH = 100;

    @Column(name = "title", nullable = false)
    private String value;

    public ExamTitle(String value) {
        validateTitleLength(value);

        this.value = value;
    }

    private void validateTitleLength(String value) {
        if (value == null || value.isBlank()) {
            throw new BadRequestException("시험 제목은 비어있을 수 없습니다.");
        }

        if (value.length() > MAX_TITLE_LENGTH) {
            throw new BadRequestException("시험 제목은 %d자 이하여야 합니다.".formatted(MAX_TITLE_LENGTH));
        }
    }
}
