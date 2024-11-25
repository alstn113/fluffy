package com.pass.exam.query.dto.question;

import com.pass.exam.query.dto.QuestionOptionResponse;
import java.util.List;

public record ChoiceQuestionResponse(
        Long id,
        String text,
        String type,
        List<QuestionOptionResponse> options
) implements QuestionResponse {
}
