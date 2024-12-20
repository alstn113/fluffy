package com.fluffy.exam.application;

import com.fluffy.exam.application.response.ExamResponse.ChoiceQuestionResponse.QuestionOptionResponse;
import com.fluffy.exam.application.response.ExamWithAnswersResponse.ChoiceQuestionWithAnswersResponse.QuestionOptionWithAnswersResponse;
import com.fluffy.exam.domain.QuestionOption;
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

    public List<QuestionOptionWithAnswersResponse> toWithAnswersResponses(List<QuestionOption> questionOptions) {
        return questionOptions.stream()
                .map(this::toWithAnswersResponse)
                .toList();
    }

    private QuestionOptionWithAnswersResponse toWithAnswersResponse(QuestionOption questionOption) {
        return new QuestionOptionWithAnswersResponse(
                questionOption.getId(),
                questionOption.getText(),
                questionOption.isCorrect()
        );
    }
}
