package com.fluffy.exam.domain;

import com.fluffy.exam.domain.dto.ExamSummaryDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExamRepositoryCustom {

    Page<ExamSummaryDto> findPublishedSummaries(Pageable pageable);

    List<ExamSummaryDto> findMySummaries(ExamStatus status, Long memberId);
}
