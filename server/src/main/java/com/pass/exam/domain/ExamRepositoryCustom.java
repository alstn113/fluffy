package com.pass.exam.domain;

import com.pass.exam.domain.dto.ExamSummaryDto;
import java.util.List;

public interface ExamRepositoryCustom {

    List<ExamSummaryDto> findSummaries();

    List<ExamSummaryDto> findMySummaries(ExamStatus status, Long memberId);
}
