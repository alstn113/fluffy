package com.pass.submission.command.application.dto;

import com.pass.global.web.Accessor;
import java.util.List;

public record SubmitAppRequest(
        String examId,
        List<AnswerAppRequest> answers,
        Accessor accessor
) {
}
