package com.fluffy.exam.ui.request;

import com.fluffy.exam.application.request.PublishExamRequest;
import com.fluffy.exam.application.request.question.QuestionRequest;
import com.fluffy.global.web.Accessor;
import java.util.List;

public record PublishExamWebRequest(List<QuestionRequest> questions) {
    public PublishExamRequest toAppRequest(Long examId, Accessor accessor) {
        return new PublishExamRequest(examId, questions, accessor);
    }
}
