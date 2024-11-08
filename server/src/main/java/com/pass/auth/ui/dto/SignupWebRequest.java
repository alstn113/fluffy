package com.pass.auth.ui.dto;

import com.pass.auth.application.dto.SignupAppRequest;

public record SignupWebRequest(String username, String password) {

    public SignupAppRequest toAppRequest() {
        return new SignupAppRequest(username, password);
    }
}
