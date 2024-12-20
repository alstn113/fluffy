package com.fluffy.submission.application.request;

import com.fluffy.global.web.Accessor;
import java.util.List;

public record SubmissionAppRequest(
        Long examId,
        List<QuestionResponseAppRequest> questionResponses,
        Accessor accessor
) {
}
