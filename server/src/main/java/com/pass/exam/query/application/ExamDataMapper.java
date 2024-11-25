package com.pass.exam.query.application;

import com.pass.exam.query.dto.ExamData;
import com.pass.exam.query.dto.ExamResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExamDataMapper {

    private final QuestionDataMapper questionDataMapper;

    public ExamResponse toResponse(ExamData examData) {
        return new ExamResponse(
                examData.getId(),
                examData.getTitle(),
                examData.getDescription(),
                questionDataMapper.toResponses(examData.getQuestions()),
                examData.getCreatedAt(),
                examData.getUpdatedAt()
        );
    }

    public List<ExamResponse> toResponses(List<ExamData> examDatas) {
        return examDatas.stream()
                .map(this::toResponse)
                .toList();
    }
}
