package com.pass.form.ui.dto;

import com.pass.form.command.application.dto.CreateFormAppRequest;

public record CreateFormWebRequest(String title) {

    public CreateFormAppRequest toAppRequest() {
        return new CreateFormAppRequest(title);
    }
}
