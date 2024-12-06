package com.fluffy.submission.ui.dto;

import com.fluffy.global.web.Accessor;
import com.fluffy.submission.application.dto.QuestionResponseAppRequest;
import com.fluffy.submission.application.dto.SubmissionAppRequest;
import java.util.List;

public record SubmissionWebRequest(List<QuestionResponseAppRequest> questionResponses) {

    public SubmissionAppRequest toAppRequest(Long examId, Accessor accessor) {
        return new SubmissionAppRequest(examId, questionResponses, accessor);
    }
}
