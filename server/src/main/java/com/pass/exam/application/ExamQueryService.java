package com.pass.exam.application;

import com.pass.exam.application.dto.ExamResponse;
import com.pass.exam.domain.Exam;
import com.pass.exam.domain.ExamRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExamQueryService {

    private final ExamRepository examRepository;
    private final ExamMapper examMapper;

    @Transactional(readOnly = true)
    public ExamResponse getExam(Long examId) {
        Exam exam = examRepository.getById(examId);

        return examMapper.toResponse(exam);
    }

    @Transactional(readOnly = true)
    public List<ExamResponse> getExams() {
        List<Exam> exams = examRepository.findAll();

        return examMapper.toResponses(exams);
    }
}
