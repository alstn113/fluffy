package com.fluffy.exam.api.request;

import com.fluffy.exam.application.request.UpdateExamDescriptionAppRequest;
import com.fluffy.global.web.Accessor;

public record UpdateExamDescriptionWebRequest(String description) {

    public UpdateExamDescriptionAppRequest toAppRequest(Long examId, Accessor accessor) {
        return new UpdateExamDescriptionAppRequest(description, examId, accessor);
    }
}
