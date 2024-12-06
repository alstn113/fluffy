package com.fluffy.exam.domain;

import com.fluffy.exam.domain.dto.ExamSummaryDto;
import java.util.List;

public interface ExamRepositoryCustom {

    List<ExamSummaryDto> findPublishedSummaries();

    List<ExamSummaryDto> findMySummaries(ExamStatus status, Long memberId);
}
