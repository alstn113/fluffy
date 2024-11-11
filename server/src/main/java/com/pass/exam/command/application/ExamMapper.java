package com.pass.exam.command.application;

import com.pass.exam.command.application.dto.CreateExamResponse;
import com.pass.exam.command.domain.Exam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExamMapper {

    private final QuestionMapper questionMapper;

    public CreateExamResponse toResponse(Exam exam) {
        return new CreateExamResponse(exam.getId(), exam.getTitle());
    }
}
