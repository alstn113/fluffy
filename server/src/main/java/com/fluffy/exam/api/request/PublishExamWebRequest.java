package com.fluffy.exam.api.request;

import com.fluffy.exam.application.request.PublishExamAppRequest;
import com.fluffy.exam.application.request.question.QuestionAppRequest;
import com.fluffy.global.web.Accessor;
import java.util.List;

public record PublishExamWebRequest(List<QuestionAppRequest> questions) {
    public PublishExamAppRequest toAppRequest(Long examId, Accessor accessor) {
        return new PublishExamAppRequest(examId, questions, accessor);
    }
}
