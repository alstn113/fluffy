package com.pass.submission.ui.dto;

import com.pass.global.web.Accessor;
import com.pass.submission.command.application.dto.AnswerAppRequest;
import com.pass.submission.command.application.dto.SubmitAppRequest;
import java.util.List;

public record SubmitWebRequest(List<AnswerAppRequest> answers) {

    public SubmitAppRequest toAppRequest(Long examId, Accessor accessor) {
        return new SubmitAppRequest(examId, answers, accessor);
    }
}
