package com.fluffy.submission.ui.request;

import com.fluffy.global.web.Accessor;
import com.fluffy.submission.application.request.QuestionResponseAppRequest;
import com.fluffy.submission.application.request.SubmissionAppRequest;
import java.util.List;

public record SubmissionWebRequest(List<QuestionResponseAppRequest> questionResponses) {

    public SubmissionAppRequest toAppRequest(Long examId, Accessor accessor) {
        return new SubmissionAppRequest(examId, questionResponses, accessor);
    }
}
