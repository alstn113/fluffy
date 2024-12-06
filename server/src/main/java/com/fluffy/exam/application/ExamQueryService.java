package com.fluffy.exam.application;

import com.fluffy.exam.application.dto.ExamResponse;
import com.fluffy.exam.application.dto.ExamWithAnswersResponse;
import com.fluffy.exam.domain.Exam;
import com.fluffy.exam.domain.ExamRepository;
import com.fluffy.exam.domain.ExamStatus;
import com.fluffy.exam.domain.dto.ExamSummaryDto;
import com.fluffy.global.exception.ForbiddenException;
import com.fluffy.global.web.Accessor;
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
    public List<ExamSummaryDto> getPublishedExamSummaries() {
        return examRepository.findPublishedSummaries();
    }

    @Transactional(readOnly = true)
    public List<ExamSummaryDto> getMyExamSummaries(ExamStatus status, Accessor accessor) {
        return examRepository.findMySummaries(status, accessor.id());
    }

    @Transactional(readOnly = true)
    public ExamResponse getExam(Long examId) {
        Exam exam = examRepository.getById(examId);

        return examMapper.toResponse(exam);
    }

    @Transactional(readOnly = true)
    public ExamWithAnswersResponse getExamWithAnswers(Long examId, Accessor accessor) {
        Exam exam = examRepository.getById(examId);

        if (exam.isNotWrittenBy(accessor.id())) {
            throw new ForbiddenException("해당 시험에 접근할 수 없습니다.");
        }

        return examMapper.toWithAnswersResponse(exam);
    }
}
