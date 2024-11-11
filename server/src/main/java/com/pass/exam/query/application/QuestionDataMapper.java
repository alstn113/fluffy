package com.pass.exam.query.application;

import com.pass.exam.command.domain.QuestionType;
import com.pass.exam.query.dto.QuestionData;
import com.pass.exam.query.dto.question.AnswerQuestionResponse;
import com.pass.exam.query.dto.question.ChoiceQuestionResponse;
import com.pass.exam.query.dto.question.QuestionResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionDataMapper {

    private final QuestionOptionDataMapper questionOptionDataMapper;

    public List<QuestionResponse> toResponses(List<QuestionData> questions) {
        return questions.stream()
                .map(this::toResponse)
                .toList();
    }

    public QuestionResponse toResponse(QuestionData questionData) {
        QuestionType questionType = QuestionType.from(questionData.getType());
        return switch (questionType) {
            case SHORT_ANSWER, LONG_ANSWER -> toAnswerResponse(questionData);
            case SINGLE_CHOICE, MULTIPLE_CHOICE, TRUE_OR_FALSE -> toChoiceResponse(questionData);
        };
    }

    private QuestionResponse toAnswerResponse(QuestionData questionData) {
        return new AnswerQuestionResponse(
                questionData.getId(),
                questionData.getText(),
                questionData.getSequence(),
                questionData.getType()
        );
    }

    private QuestionResponse toChoiceResponse(QuestionData questionData) {
        return new ChoiceQuestionResponse(
                questionData.getId(),
                questionData.getText(),
                questionData.getSequence(),
                questionData.getType(),
                questionOptionDataMapper.toResponses(questionData.getOptions())
        );
    }
}
