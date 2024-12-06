package com.fluffy.exam.ui.dto;

import com.fluffy.exam.application.dto.UpdateExamTitleAppRequest;
import com.fluffy.global.web.Accessor;

public record UpdateExamTitleWebRequest(String title) {

    public UpdateExamTitleAppRequest toAppRequest(Long examId, Accessor accessor) {
        return new UpdateExamTitleAppRequest(title, examId, accessor);
    }
}
