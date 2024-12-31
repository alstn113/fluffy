package com.fluffy.exam.application;

import com.fluffy.exam.application.response.ExamDetailResponse;
import com.fluffy.exam.application.response.ExamWithAnswersResponse;
import com.fluffy.exam.domain.Exam;
import com.fluffy.exam.domain.ExamRepository;
import com.fluffy.exam.domain.ExamStatus;
import com.fluffy.exam.domain.dto.ExamSummaryDto;
import com.fluffy.exam.domain.dto.SubmittedExamSummaryDto;
import com.fluffy.global.exception.ForbiddenException;
import com.fluffy.global.response.PageResponse;
import com.fluffy.global.web.Accessor;
import com.fluffy.submission.domain.dto.MySubmissionSummaryDto;
import java.util.List;
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
        Page<ExamSummaryDto> summaries = examRepository.findPublishedExamSummaries(pageable);

        return PageResponse.of(summaries);
    }

    @Transactional(readOnly = true)
    public PageResponse<ExamSummaryDto> getMyExamSummaries(Pageable pageable, ExamStatus status, Accessor accessor) {
        Page<ExamSummaryDto> summaries = examRepository.findMyExamSummaries(pageable, status, accessor.id());

        return PageResponse.of(summaries);
    }

    @Transactional(readOnly = true)
    public ExamDetailResponse getExamDetail(Long examId) {
        Exam exam = examRepository.findByIdOrThrow(examId);

        return examMapper.toDetailResponse(exam);
    }

    @Transactional(readOnly = true)
    public ExamWithAnswersResponse getExamWithAnswers(Long examId, Accessor accessor) {
        Exam exam = examRepository.findByIdOrThrow(examId);

        if (exam.isNotWrittenBy(accessor.id())) {
            throw new ForbiddenException("해당 시험에 접근할 수 없습니다.");
        }

        return examMapper.toWithAnswersResponse(exam);
    }

    @Transactional(readOnly = true)
    public PageResponse<SubmittedExamSummaryDto> getSubmittedExamSummaries(Pageable pageable, Accessor accessor) {
        Page<SubmittedExamSummaryDto> summaries = examRepository.findSubmittedExamSummaries(pageable, accessor.id());

        return PageResponse.of(summaries);
    }
}
