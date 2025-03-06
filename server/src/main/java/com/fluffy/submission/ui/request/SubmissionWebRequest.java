package com.fluffy.submission.ui.request;

import com.fluffy.global.web.Accessor;
import com.fluffy.submission.application.request.QuestionResponseRequest;
import com.fluffy.submission.application.request.SubmissionRequest;
import java.util.List;

public record SubmissionWebRequest(List<QuestionResponseRequest> questionResponses) {

    public SubmissionRequest toAppRequest(Long examId, Accessor accessor) {
        return new SubmissionRequest(examId, questionResponses, accessor);
    }
}
