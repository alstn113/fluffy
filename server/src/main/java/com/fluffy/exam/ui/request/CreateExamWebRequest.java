package com.fluffy.exam.ui.request;

import com.fluffy.exam.application.request.CreateExamAppRequest;
import com.fluffy.global.web.Accessor;

public record CreateExamWebRequest(String title) {

    public CreateExamAppRequest toAppRequest(Accessor accessor) {
        return new CreateExamAppRequest(title, accessor);
    }
}
