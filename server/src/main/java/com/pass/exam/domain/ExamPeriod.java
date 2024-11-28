package com.pass.exam.domain;

import com.pass.global.exception.BadRequestException;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
public class ExamPeriod {

    @Column
    private LocalDateTime startAt;

    @Column
    private LocalDateTime endAt;

    public static ExamPeriod create(@Nullable LocalDateTime startAt, @Nullable LocalDateTime endAt) {
        LocalDateTime validatedStartAt = validateAndGetStartAt(startAt);
        validateEndAt(validatedStartAt, endAt);

        return new ExamPeriod(validatedStartAt, endAt);
    }

    private static void validateEndAt(LocalDateTime startAt, @Nullable LocalDateTime endAt) {
        if (endAt != null && startAt.isAfter(endAt)) {
            throw new BadRequestException("시작 시간은 종료 시간 이전이어야 합니다.");
        }
    }

    private static LocalDateTime validateAndGetStartAt(@Nullable LocalDateTime startAt) {
        LocalDateTime now = LocalDateTime.now();

        if (startAt != null && startAt.isBefore(now)) {
            throw new BadRequestException("시작 시간은 현재 시간 이후여야 합니다.");
        }

        if (startAt == null) {
            return now;
        }

        return startAt;
    }

    private ExamPeriod(LocalDateTime startAt, LocalDateTime endAt) {
        this.startAt = startAt;
        this.endAt = endAt;
    }
}
