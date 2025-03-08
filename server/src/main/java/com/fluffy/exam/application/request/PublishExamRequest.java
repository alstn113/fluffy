package com.fluffy.exam.application.request;

import com.fluffy.exam.application.request.question.QuestionRequest;
import com.fluffy.global.web.Accessor;
import java.util.List;

public record PublishExamRequest(
        Long examId,
        List<QuestionRequest> questions,
        Accessor accessor
) {
}
