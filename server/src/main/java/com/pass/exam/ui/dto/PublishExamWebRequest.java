package com.pass.exam.ui.dto;

import com.pass.exam.command.application.dto.PublishExamAppRequest;
import com.pass.exam.command.application.dto.question.QuestionAppRequest;
import com.pass.global.web.Accessor;
import java.util.List;

public record PublishExamWebRequest(List<QuestionAppRequest> questions) {

    public PublishExamAppRequest toAppRequest(Long examId, Accessor accessor) {
        return new PublishExamAppRequest(examId, questions, accessor);
    }
}
