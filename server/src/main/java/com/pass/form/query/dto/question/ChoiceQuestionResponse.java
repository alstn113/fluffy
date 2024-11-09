package com.pass.form.query.dto.question;

import com.pass.form.query.dto.QuestionOptionDataResponse;
import java.util.List;

public record ChoiceQuestionResponse(
        Long id,
        String text,
        int sequence,
        String type,
        List<QuestionOptionDataResponse> options
) implements QuestionResponse {
}
