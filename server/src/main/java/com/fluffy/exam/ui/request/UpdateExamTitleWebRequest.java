package com.fluffy.exam.ui.request;

import com.fluffy.exam.application.request.UpdateExamTitleAppRequest;
import com.fluffy.global.web.Accessor;

public record UpdateExamTitleWebRequest(String title) {

    public UpdateExamTitleAppRequest toAppRequest(Long examId, Accessor accessor) {
        return new UpdateExamTitleAppRequest(title, examId, accessor);
    }
}
