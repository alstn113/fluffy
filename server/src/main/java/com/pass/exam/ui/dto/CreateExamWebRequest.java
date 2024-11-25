package com.pass.exam.ui.dto;

import com.pass.exam.application.dto.CreateExamAppRequest;
import com.pass.global.web.Accessor;

public record CreateExamWebRequest(String title) {

    public CreateExamAppRequest toAppRequest(Accessor accessor) {
        return new CreateExamAppRequest(title, accessor);
    }
}
