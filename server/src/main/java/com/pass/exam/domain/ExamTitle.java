package com.pass.exam.domain;

import com.pass.global.exception.BadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExamTitle {

    private static final int MAX_TITLE_LENGTH = 100;

    @Column(nullable = false)
    private String title;

    public ExamTitle(String title) {
        validateTitleLength(title);

        this.title = title;
    }

    private void validateTitleLength(String title) {
        if (title == null || title.isBlank()) {
            throw new BadRequestException("시험 제목은 비어있을 수 없습니다.");
        }

        if (title.length() > MAX_TITLE_LENGTH) {
            throw new BadRequestException("시험 제목은 %d자 이하여야 합니다.".formatted(MAX_TITLE_LENGTH));
        }
    }

    public String getValue() {
        return title;
    }
}
