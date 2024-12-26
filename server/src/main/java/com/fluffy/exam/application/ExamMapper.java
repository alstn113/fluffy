package com.fluffy.exam.application;

import com.fluffy.exam.application.response.CreateExamResponse;
import com.fluffy.exam.application.response.ExamDetailResponse;
import com.fluffy.exam.application.response.ExamWithAnswersResponse;
import com.fluffy.exam.domain.Exam;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExamMapper {

    private final QuestionMapper questionMapper;

    public CreateExamResponse toCreateResponse(Exam exam) {
        return new CreateExamResponse(exam.getId(), exam.getTitle());
    }

    public List<ExamDetailResponse> toResponses(List<Exam> exams) {
        return exams.stream()
                .map(this::toDetailResponse)
                .toList();
    }

    public ExamDetailResponse toDetailResponse(Exam exam) {
        return new ExamDetailResponse(
                exam.getId(),
                exam.getTitle(),
                exam.getDescription(),
                exam.getStatus().name(),
                questionMapper.toResponses(exam.getQuestionGroup().toList()),
                exam.getCreatedAt(),
                exam.getUpdatedAt()
        );
    }

    public ExamWithAnswersResponse toWithAnswersResponse(Exam exam) {
        return new ExamWithAnswersResponse(
                exam.getId(),
                exam.getTitle(),
                exam.getDescription(),
                exam.getStatus().name(),
                questionMapper.toWithAnswersResponses(exam.getQuestionGroup().toList()),
                exam.getCreatedAt(),
                exam.getUpdatedAt()
        );
    }
}
