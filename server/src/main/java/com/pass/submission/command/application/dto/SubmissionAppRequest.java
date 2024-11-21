package com.pass.submission.command.application.dto;

import com.pass.global.web.Accessor;
import java.util.List;

public record SubmissionAppRequest(
        Long examId,
        List<QuestionResponseAppRequest> questionResponses,
        Accessor accessor
) {
}
