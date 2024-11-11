package com.pass.form.command.application.dto;

import com.pass.form.command.application.dto.question.QuestionAppRequest;
import com.pass.global.web.Accessor;
import java.util.List;

public record PublishFormAppRequest(
        String formId,
        List<QuestionAppRequest> questions,
        Accessor accessor
) {
}
