package com.fluffy.exam.domain;

import com.fluffy.exam.domain.dto.ExamSummaryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExamRepositoryCustom {

    Page<ExamSummaryDto> findPublishedExamSummaries(Pageable pageable);

    Page<ExamSummaryDto> findMyExamSummaries(Pageable pageable, ExamStatus status, Long memberId);
}
