package com.fluffy.exam.ui.request;

import com.fluffy.exam.application.request.UpdateExamDescriptionRequest;
import com.fluffy.global.web.Accessor;

public record UpdateExamDescriptionWebRequest(String description) {

    public UpdateExamDescriptionRequest toAppRequest(Long examId, Accessor accessor) {
        return new UpdateExamDescriptionRequest(description, examId, accessor);
    }
}
