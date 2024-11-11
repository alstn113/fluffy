package com.pass.form.ui.dto;

import com.pass.form.command.application.dto.CreateFormAppRequest;
import com.pass.global.web.Accessor;

public record CreateFormWebRequest(String title) {

    public CreateFormAppRequest toAppRequest(Accessor accessor) {
        return new CreateFormAppRequest(title, accessor);
    }
}
