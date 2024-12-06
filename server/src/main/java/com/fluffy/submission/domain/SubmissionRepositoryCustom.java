package com.fluffy.submission.domain;

import com.fluffy.submission.domain.dto.SubmissionSummaryDto;
import java.util.List;

public interface SubmissionRepositoryCustom {

    List<SubmissionSummaryDto> findSummariesByExamId(Long examId);
}
