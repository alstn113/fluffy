package com.pass.submission.command.application.dto;

import java.util.List;
import org.springframework.lang.Nullable;

public record AnswerAppRequest(
        Long questionId,
        @Nullable String text,
        @Nullable List<ChoiceAppRequest> choices
) {
}
