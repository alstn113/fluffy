package com.fluffy.exam.application.dto;

import com.fluffy.exam.application.dto.question.QuestionAppRequest;
import com.fluffy.global.web.Accessor;
import java.util.List;

public record UpdateExamQuestionsAppRequest(
        Long examId,
        List<QuestionAppRequest> questions,
        Accessor accessor
) {
}
