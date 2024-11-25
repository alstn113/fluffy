package com.pass.exam.application;

import com.pass.exam.application.dto.ExamResponse;
import com.pass.exam.application.dto.ExamWithAnswersResponse;
import com.pass.exam.application.dto.question.CreateExamResponse;
import com.pass.exam.domain.Exam;
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

    public List<ExamResponse> toResponses(List<Exam> exams) {
        return exams.stream()
                .map(this::toResponse)
                .toList();
    }

    public ExamResponse toResponse(Exam exam) {
        return new ExamResponse(
                exam.getId(),
                exam.getTitle(),
                exam.getDescription(),
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
                questionMapper.toWithAnswersResponses(exam.getQuestionGroup().toList()),
                exam.getCreatedAt(),
                exam.getUpdatedAt()
        );
    }
}
