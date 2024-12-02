package com.pass.exam.ui.dto;

import com.pass.exam.application.dto.PublishExamAppRequest;
import com.pass.global.web.Accessor;
import java.time.LocalDateTime;

public record PublishExamWebRequest(LocalDateTime startDate, LocalDateTime endDate) {
    public PublishExamAppRequest toAppRequest(Long examId, Accessor accessor) {
        return new PublishExamAppRequest(examId, startDate, endDate, accessor);
    }
}
