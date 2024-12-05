package com.pass.exam.domain;

import com.pass.global.exception.BadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExamDescription {

    private static final int MAX_DESCRIPTION_LENGTH = 2000;

    @Column(nullable = false)
    private String description;

    public ExamDescription(String description) {
        validateDescriptionLength(description);

        this.description = description;
    }

    private void validateDescriptionLength(String title) {
        if (title == null) {
            throw new BadRequestException("시험 설명은 필수입니다.");
        }

        if (title.length() > MAX_DESCRIPTION_LENGTH) {
            throw new BadRequestException("시험 설명은 %d자 이하여야 합니다.".formatted(MAX_DESCRIPTION_LENGTH));
        }
    }

    public String getValue() {
        return description;
    }
}
