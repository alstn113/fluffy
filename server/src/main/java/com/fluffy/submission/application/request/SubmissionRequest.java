package com.fluffy.submission.application.request;

import com.fluffy.global.web.Accessor;
import java.util.List;

public record SubmissionRequest(
        Long examId,
        List<QuestionResponseRequest> questionResponses,
        Accessor accessor
) {
}
