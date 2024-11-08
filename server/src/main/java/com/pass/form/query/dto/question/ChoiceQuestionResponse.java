package com.pass.form.query.dto.question;

import com.pass.form.query.dto.QuestionOptionResponse;
import java.util.List;

public record ChoiceQuestionResponse(
        Long id,
        String text,
        int sequence,
        String type,
        List<QuestionOptionResponse> options
) implements QuestionResponse {
}
