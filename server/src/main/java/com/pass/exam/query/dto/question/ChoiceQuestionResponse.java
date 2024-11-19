package com.pass.exam.query.dto.question;

import com.pass.exam.query.dto.QuestionOptionDataResponse;
import java.util.List;

public record ChoiceQuestionResponse(
        Long id,
        String text,
        String type,
        List<QuestionOptionDataResponse> options
) implements QuestionResponse {
}
