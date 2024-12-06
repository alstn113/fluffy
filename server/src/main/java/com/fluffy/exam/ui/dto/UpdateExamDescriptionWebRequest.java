package com.fluffy.exam.ui.dto;

import com.fluffy.exam.application.dto.UpdateExamDescriptionAppRequest;
import com.fluffy.global.web.Accessor;

public record UpdateExamDescriptionWebRequest(String description) {

    public UpdateExamDescriptionAppRequest toAppRequest(Long examId, Accessor accessor) {
        return new UpdateExamDescriptionAppRequest(description, examId, accessor);
    }
}
