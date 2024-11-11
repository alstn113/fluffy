package com.pass.form.command.application.dto;

import com.pass.global.web.Accessor;

public record CreateFormAppRequest(String title, Accessor accessor) {
}
