package com.pass.submission.domain.dto;

import com.pass.submission.domain.Choice;
import com.querydsl.core.annotations.QueryProjection;
import java.util.List;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnswerDto {

    private Long id;
    private Long questionId;
    private String text;
    private List<Choice> choices;

    @QueryProjection
    public AnswerDto(Long id, Long questionId, String text, List<Choice> choices) {
        this.id = id;
        this.questionId = questionId;
        this.text = text;
        this.choices = choices;
    }
}
