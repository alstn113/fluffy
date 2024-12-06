package com.fluffy.exam.application.dto;

import com.fluffy.exam.application.dto.question.QuestionAppRequest;
import com.fluffy.global.web.Accessor;
import java.time.LocalDateTime;
import java.util.List;

public record PublishExamAppRequest(
        Long examId,
        List<QuestionAppRequest> questions,
        LocalDateTime startAt,
        LocalDateTime endAt,
        Accessor accessor
) {
}
