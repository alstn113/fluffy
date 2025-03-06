package com.fluffy.exam.ui.request;

import com.fluffy.exam.application.request.CreateExamRequest;
import com.fluffy.global.web.Accessor;

public record CreateExamWebRequest(String title) {

    public CreateExamRequest toAppRequest(Accessor accessor) {
        return new CreateExamRequest(title, accessor);
    }
}
