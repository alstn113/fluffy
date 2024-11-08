package com.pass.auth.ui.dto;

import com.pass.auth.application.dto.LoginAppRequest;

public record LoginWebRequest(String username, String password) {

    public LoginAppRequest toAppRequest() {
        return new LoginAppRequest(username, password);
    }
}
