package com.fluffy.unit.exam.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.fluffy.exam.domain.ExamPeriod;
import com.fluffy.global.exception.BadRequestException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ExamPeriodTest {

    @Test
    @DisplayName("시작 시간과 종료 시간은 분 단위로 설정된다.")
    void startAtAndEndAtShouldBeTruncatedToMinutes() {
        // given
        LocalDateTime startAt = LocalDateTime.now();
        System.out.println(startAt);
        LocalDateTime endAt = startAt.plusMinutes(1);

        // when
        ExamPeriod examPeriod = ExamPeriod.create(startAt, endAt);

        // then
        assertAll(
                () -> assertThat(examPeriod.getStartAt()).isEqualTo(startAt.truncatedTo(ChronoUnit.MINUTES)),
                () -> assertThat(examPeriod.getEndAt()).isEqualTo(endAt.truncatedTo(ChronoUnit.MINUTES))
        );
    }

    @Test
    @DisplayName("시작 시간이 null이고, 종료 시간이 null인 경우, 시작 시간은 현재 시간으로 설정된다.")
    void setStartAtToNowWhenStartAtIsNullAndEndAtIsNull() {
        // given
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);

        // when
        ExamPeriod examPeriod = ExamPeriod.create(null, null);

        // then
        assertAll(
                () -> assertThat(examPeriod.getStartAt()).isEqualTo(now),
                () -> assertThat(examPeriod.getEndAt()).isNull()
        );
    }

    @Test
    @DisplayName("시작 시간은 현재 시간보다 같거나 이후여야 한다.")
    void startAtShouldBeSameOrAfterNow() {
        // given
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        LocalDateTime startAt = now.minusMinutes(1);

        // when & then
        assertThatThrownBy(() -> ExamPeriod.create(startAt, null))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("시작 시간은 현재 시간과 같거나 이후여야 합니다.");
    }

    @Test
    @DisplayName("시작 시간은 종료 시간보다 이전이어야 한다.")
    void startAtShouldBeBeforeEndAt() {
        // given
        LocalDateTime startAt = LocalDateTime.now();
        LocalDateTime endAt = startAt.minusMinutes(1);

        // when & then
        assertThatThrownBy(() -> ExamPeriod.create(startAt, endAt))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("시작 시간은 종료 시간 이전이어야 합니다.");
    }
}
