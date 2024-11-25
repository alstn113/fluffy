package com.pass.exam.application;

import com.pass.exam.application.dto.question.response.QuestionOptionResponse;
import com.pass.exam.domain.QuestionOption;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionOptionMapper {

    public List<QuestionOptionResponse> toResponses(List<QuestionOption> questionOption) {
        return questionOption.stream()
                .map(this::toResponse)
                .toList();
    }

    private QuestionOptionResponse toResponse(QuestionOption questionOption) {
        return new QuestionOptionResponse(
                questionOption.getId(),
                questionOption.getText()
        );
    }
}
