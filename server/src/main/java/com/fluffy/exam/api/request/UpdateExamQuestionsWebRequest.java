package com.fluffy.exam.api.request;

import com.fluffy.exam.application.request.UpdateExamQuestionsAppRequest;
import com.fluffy.exam.application.request.question.QuestionAppRequest;
import com.fluffy.global.web.Accessor;
import java.util.List;

public record UpdateExamQuestionsWebRequest(List<QuestionAppRequest> questions) {

    public UpdateExamQuestionsAppRequest toAppRequest(Long examId, Accessor accessor) {
        return new UpdateExamQuestionsAppRequest(examId, questions, accessor);
    }
}
