package com.pass.exam.ui.dto;

import com.pass.exam.application.dto.UpdateExamInfoAppRequest;
import com.pass.global.web.Accessor;

public record UpdateExamInfoWebRequest(
        String title,
        String description
) {

    public UpdateExamInfoAppRequest toAppRequest(Long examId, Accessor accessor) {
        return new UpdateExamInfoAppRequest(title, description, examId, accessor);
    }
}
