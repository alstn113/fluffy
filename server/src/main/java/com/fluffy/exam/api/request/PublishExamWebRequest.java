package com.fluffy.exam.api.request;

import com.fluffy.exam.application.request.PublishExamAppRequest;
import com.fluffy.exam.application.request.question.QuestionAppRequest;
import com.fluffy.global.web.Accessor;
import java.time.LocalDateTime;
import java.util.List;

public record PublishExamWebRequest(
        List<QuestionAppRequest> questions,
        LocalDateTime startAt,
        LocalDateTime endAt
) {
    public PublishExamAppRequest toAppRequest(Long examId, Accessor accessor) {
        return new PublishExamAppRequest(examId, questions, startAt, endAt, accessor);
    }
}
