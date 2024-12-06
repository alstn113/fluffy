package com.fluffy.exam.domain;

import com.fluffy.global.exception.BadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ExamDescription {

    private static final int MAX_DESCRIPTION_LENGTH = 2000;

    @Column(name = "description", nullable = false)
    private String value;

    public ExamDescription(String value) {
        validateDescriptionLength(value);

        this.value = value;
    }

    private void validateDescriptionLength(String value) {
        if (value == null) {
            throw new BadRequestException("시험 설명은 필수입니다.");
        }

        if (value.length() > MAX_DESCRIPTION_LENGTH) {
            throw new BadRequestException("시험 설명은 %d자 이하여야 합니다.".formatted(MAX_DESCRIPTION_LENGTH));
        }
    }
}
