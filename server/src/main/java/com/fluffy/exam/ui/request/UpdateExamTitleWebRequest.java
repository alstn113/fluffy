package com.fluffy.exam.ui.request;

import com.fluffy.exam.application.request.UpdateExamTitleRequest;
import com.fluffy.global.web.Accessor;

public record UpdateExamTitleWebRequest(String title) {

    public UpdateExamTitleRequest toAppRequest(Long examId, Accessor accessor) {
        return new UpdateExamTitleRequest(title, examId, accessor);
    }
}
