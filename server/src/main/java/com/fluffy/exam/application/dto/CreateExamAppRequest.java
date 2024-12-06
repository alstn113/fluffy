package com.fluffy.exam.application.dto;

import com.fluffy.global.web.Accessor;

public record CreateExamAppRequest(String title, Accessor accessor) {
}
