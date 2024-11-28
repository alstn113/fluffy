package com.pass.submission.domain.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChoiceDto {

    private Long questionOptionId;
    private String text;

    @QueryProjection
    public ChoiceDto(Long questionOptionId, String text) {
        this.questionOptionId = questionOptionId;
        this.text = text;
    }
}
