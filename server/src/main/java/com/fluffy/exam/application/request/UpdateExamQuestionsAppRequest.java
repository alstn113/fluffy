package com.fluffy.exam.application.request;

import com.fluffy.exam.application.request.question.QuestionAppRequest;
import com.fluffy.global.web.Accessor;
import java.util.List;

public record UpdateExamQuestionsAppRequest(
        Long examId,
        List<QuestionAppRequest> questions,
        Accessor accessor
) {
}
