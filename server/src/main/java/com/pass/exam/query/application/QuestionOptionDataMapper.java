package com.pass.exam.query.application;

import com.pass.exam.query.dto.QuestionOptionData;
import com.pass.exam.query.dto.QuestionOptionResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionOptionDataMapper {

    public List<QuestionOptionResponse> toResponses(List<QuestionOptionData> questionOptionData) {
        return questionOptionData.stream()
                .map(this::toResponse)
                .toList();
    }

    private QuestionOptionResponse toResponse(QuestionOptionData questionOptionData) {
        return new QuestionOptionResponse(
                questionOptionData.getId(),
                questionOptionData.getText()
        );
    }
}
