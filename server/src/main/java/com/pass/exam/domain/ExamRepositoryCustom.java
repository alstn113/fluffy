package com.pass.exam.domain;

import com.pass.exam.domain.dto.ExamSummaryDto;
import java.util.List;

public interface ExamRepositoryCustom {

    List<ExamSummaryDto> findAllExamSummaries();

    List<ExamSummaryDto> findMyExamSummaries(ExamStatus status, Long memberId);
}
