package com.fluffy.exam.domain;

import com.fluffy.global.exception.BadRequestException;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
        LocalDateTime validatedEndAt = validateAndGetEndAt(validatedStartAt, endAt);

        return new ExamPeriod(validatedStartAt, validatedEndAt);
    }

    private static LocalDateTime validateAndGetStartAt(@Nullable LocalDateTime startAt) {
        LocalDateTime nowTruncated = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);

        if (startAt == null) {
            return nowTruncated;
        }

        // 시작 시간은 현재 시간과 같거나 이후여야 함.
        LocalDateTime startAtTruncated = startAt.truncatedTo(ChronoUnit.MINUTES);
        if (nowTruncated.isAfter(startAtTruncated)) {
            throw new BadRequestException("시작 시간은 현재 시간과 같거나 이후여야 합니다.");
        }

        return startAtTruncated;
    }

    private static LocalDateTime validateAndGetEndAt(LocalDateTime startAtTruncated, @Nullable LocalDateTime endAt) {
        if (endAt == null) {
            return null;
        }

        // 시작 시간은 종료 시간과 같을 수 없고, 이전이어야 함.
        LocalDateTime endAtTruncated = endAt.truncatedTo(ChronoUnit.MINUTES);
        if (startAtTruncated.isAfter(endAtTruncated)) {
            throw new BadRequestException("시작 시간은 종료 시간 이전이어야 합니다.");
        }

        return endAtTruncated;
    }

    private ExamPeriod(LocalDateTime startAt, LocalDateTime endAt) {
        this.startAt = startAt;
        this.endAt = endAt;
    }
}
