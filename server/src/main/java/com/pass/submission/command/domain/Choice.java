package com.pass.submission.command.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Choice {

    @Column(nullable = false)
    private Long questionOptionId;

    public Choice(Long questionOptionId) {
        this.questionOptionId = questionOptionId;
    }
}
