package com.fluffy.exam.application.request;

import com.fluffy.global.web.Accessor;

public record CreateExamAppRequest(String title, Accessor accessor) {
}
