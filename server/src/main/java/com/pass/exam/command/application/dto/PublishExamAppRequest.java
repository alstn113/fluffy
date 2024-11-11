package com.pass.exam.command.application.dto;

import com.pass.exam.command.application.dto.question.QuestionAppRequest;
import com.pass.global.web.Accessor;
import java.util.List;

public record PublishExamAppRequest(
        String examId,
        List<QuestionAppRequest> questions,
        Accessor accessor
) {
}
