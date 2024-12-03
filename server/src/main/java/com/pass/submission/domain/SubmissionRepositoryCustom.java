package com.pass.submission.domain;

import com.pass.submission.domain.dto.SubmissionSummaryDto;
import java.util.List;

public interface SubmissionRepositoryCustom {

    List<SubmissionSummaryDto> findSummariesByExamId(Long examId);
}
