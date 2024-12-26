package com.fluffy.exam.application;

import com.fluffy.exam.application.response.ExamResponse;
import com.fluffy.exam.application.response.ExamWithAnswersResponse;
import com.fluffy.exam.domain.Exam;
import com.fluffy.exam.domain.ExamRepository;
import com.fluffy.exam.domain.ExamStatus;
import com.fluffy.exam.domain.dto.ExamSummaryDto;
import com.fluffy.global.exception.ForbiddenException;
import com.fluffy.global.response.PageResponse;
import com.fluffy.global.web.Accessor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExamQueryService {

    private final ExamRepository examRepository;
    private final ExamMapper examMapper;

    @Transactional(readOnly = true)
    public PageResponse<ExamSummaryDto> getPublishedExamSummaries(Pageable pageable) {
        Page<ExamSummaryDto> examSummaries = examRepository.findPublishedExamSummaries(pageable);

        return PageResponse.of(examSummaries);
    }

    @Transactional(readOnly = true)
    public PageResponse<ExamSummaryDto> getMyExamSummaries(Pageable pageable, ExamStatus status, Accessor accessor) {
        Page<ExamSummaryDto> examSummaries = examRepository.findMyExamSummaries(pageable, status, accessor.id());

        return PageResponse.of(examSummaries);
    }

    @Transactional(readOnly = true)
    public ExamResponse getExam(Long examId) {
        Exam exam = examRepository.findByIdOrThrow(examId);

        return examMapper.toResponse(exam);
    }

    @Transactional(readOnly = true)
    public ExamWithAnswersResponse getExamWithAnswers(Long examId, Accessor accessor) {
        Exam exam = examRepository.findByIdOrThrow(examId);

        if (exam.isNotWrittenBy(accessor.id())) {
            throw new ForbiddenException("해당 시험에 접근할 수 없습니다.");
        }

        return examMapper.toWithAnswersResponse(exam);
    }
}
