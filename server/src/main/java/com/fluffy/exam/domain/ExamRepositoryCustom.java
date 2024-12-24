package com.fluffy.exam.domain;

import com.fluffy.exam.domain.dto.ExamSummaryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExamRepositoryCustom {

    Page<ExamSummaryDto> findPublishedSummaries(Pageable pageable);

    Page<ExamSummaryDto> findMySummaries(ExamStatus status, Pageable pageable, Long memberId);
}
