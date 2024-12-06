package com.fluffy.exam.ui.dto;

import com.fluffy.exam.application.dto.UpdateExamQuestionsAppRequest;
import com.fluffy.exam.application.dto.question.QuestionAppRequest;
import com.fluffy.global.web.Accessor;
import java.util.List;

public record UpdateExamQuestionsWebRequest(List<QuestionAppRequest> questions) {

    public UpdateExamQuestionsAppRequest toAppRequest(Long examId, Accessor accessor) {
        return new UpdateExamQuestionsAppRequest(examId, questions, accessor);
    }
}
