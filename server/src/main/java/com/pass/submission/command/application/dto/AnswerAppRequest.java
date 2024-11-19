package com.pass.submission.command.application.dto;

import java.util.List;

public record AnswerAppRequest(
        String text,
        List<Integer> choices
) {
}
