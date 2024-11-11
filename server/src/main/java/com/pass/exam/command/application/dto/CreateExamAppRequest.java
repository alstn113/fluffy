package com.pass.exam.command.application.dto;

import com.pass.global.web.Accessor;

public record CreateExamAppRequest(String title, Accessor accessor) {
}
