package com.pass.submission.command.application.dto;

import com.pass.global.web.Accessor;
import java.util.List;

public record SubmitAppRequest(
        Long examId,
        List<AnswerAppRequest> answers,
        Accessor accessor
) {
}
