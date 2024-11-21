package com.pass.exam.ui.dto;

import com.pass.exam.command.application.dto.UpdateExamQuestionsAppRequest;
import com.pass.exam.command.application.dto.question.QuestionAppRequest;
import com.pass.global.web.Accessor;
import java.util.List;

public record UpdateExamQuestionsWebRequest(List<QuestionAppRequest> questions) {

    public UpdateExamQuestionsAppRequest toAppRequest(Long examId, Accessor accessor) {
        return new UpdateExamQuestionsAppRequest(examId, questions, accessor);
    }
}
