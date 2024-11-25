package com.pass.exam.application.dto;

import com.pass.exam.application.dto.question.request.QuestionAppRequest;
import com.pass.global.web.Accessor;
import java.util.List;

public record UpdateExamQuestionsAppRequest(
        Long examId,
        List<QuestionAppRequest> questions,
        Accessor accessor
) {
}
