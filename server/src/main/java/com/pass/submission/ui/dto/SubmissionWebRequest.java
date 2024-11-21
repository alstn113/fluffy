package com.pass.submission.ui.dto;

import com.pass.global.web.Accessor;
import com.pass.submission.command.application.dto.QuestionResponseAppRequest;
import com.pass.submission.command.application.dto.SubmissionAppRequest;
import java.util.List;

public record SubmissionWebRequest(List<QuestionResponseAppRequest> questionResponses) {

    public SubmissionAppRequest toAppRequest(Long examId, Accessor accessor) {
        return new SubmissionAppRequest(examId, questionResponses, accessor);
    }
}
