package com.pass.form.query.application;

import com.pass.form.query.dto.QuestionOptionData;
import com.pass.form.query.dto.QuestionOptionDataResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionOptionDataMapper {

    public List<QuestionOptionDataResponse> toResponses(List<QuestionOptionData> questionOptionData) {
        return questionOptionData.stream()
                .map(this::toResponse)
                .toList();
    }

    private QuestionOptionDataResponse toResponse(QuestionOptionData questionOptionData) {
        return new QuestionOptionDataResponse(
                questionOptionData.getId(),
                questionOptionData.getText(),
                questionOptionData.getSequence()
        );
    }
}
