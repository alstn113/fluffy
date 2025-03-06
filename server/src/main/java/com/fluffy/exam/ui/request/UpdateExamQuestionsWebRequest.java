package com.fluffy.exam.ui.request;

import com.fluffy.exam.application.request.UpdateExamQuestionsRequest;
import com.fluffy.exam.application.request.question.QuestionRequest;
import com.fluffy.global.web.Accessor;
import java.util.List;

public record UpdateExamQuestionsWebRequest(List<QuestionRequest> questions) {

    public UpdateExamQuestionsRequest toAppRequest(Long examId, Accessor accessor) {
        return new UpdateExamQuestionsRequest(examId, questions, accessor);
    }
}
