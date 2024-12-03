package com.pass.exam.ui.dto;

import com.pass.exam.application.dto.PublishExamAppRequest;
import com.pass.exam.application.dto.question.QuestionAppRequest;
import com.pass.global.web.Accessor;
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
