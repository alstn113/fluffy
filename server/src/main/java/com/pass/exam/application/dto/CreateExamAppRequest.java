package com.pass.exam.application.dto;

import com.pass.global.web.Accessor;

public record CreateExamAppRequest(String title, Accessor accessor) {
}
