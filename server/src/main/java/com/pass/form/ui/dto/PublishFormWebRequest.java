package com.pass.form.ui.dto;

import com.pass.form.command.application.dto.PublishFormAppRequest;
import com.pass.form.command.application.dto.question.QuestionAppRequest;
import com.pass.global.web.Accessor;
import java.util.List;

public record PublishFormWebRequest(
        List<QuestionAppRequest> questions
) {

    public PublishFormAppRequest toAppRequest(String formId, Accessor accessor) {
        return new PublishFormAppRequest(formId, questions, accessor);
    }
}
