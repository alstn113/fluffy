package com.pass.form.command.application.dto;

import com.pass.form.command.application.dto.question.QuestionAppRequest;
import java.util.List;

public record PublishFormAppRequest(
        String formId,
        List<QuestionAppRequest> questions
) {
}
