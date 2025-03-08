package com.fluffy.exam.application.request;

import com.fluffy.global.web.Accessor;

public record CreateExamRequest(String title, Accessor accessor) {
}
