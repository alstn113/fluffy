package com.pass.exam.application;

import com.pass.exam.application.dto.ExamResponse;
import com.pass.exam.domain.Exam;
import com.pass.exam.domain.ExamRepository;
import com.pass.exam.application.dto.ExamWithAnswersResponse;
import com.pass.global.exception.ForbiddenException;
import com.pass.global.web.Accessor;
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

    public ExamWithAnswersResponse getExamWithAnswers(Long examId, Accessor accessor) {
        Exam exam = examRepository.getById(examId);

        if (exam.isNotWrittenBy(accessor.id())) {
            throw new ForbiddenException("해당 시험에 접근할 수 없습니다.");
        }

        return examMapper.toWithAnswersResponse(exam);
    }
}
