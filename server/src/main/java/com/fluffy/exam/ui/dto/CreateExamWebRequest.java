package com.fluffy.exam.ui.dto;

import com.fluffy.exam.application.dto.CreateExamAppRequest;
import com.fluffy.global.web.Accessor;

public record CreateExamWebRequest(String title) {

    public CreateExamAppRequest toAppRequest(Accessor accessor) {
        return new CreateExamAppRequest(title, accessor);
    }
}
