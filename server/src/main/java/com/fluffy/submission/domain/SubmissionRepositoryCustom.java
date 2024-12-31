package com.fluffy.submission.domain;

import com.fluffy.submission.domain.dto.MySubmissionSummaryDto;
import com.fluffy.submission.domain.dto.SubmissionSummaryDto;
import java.util.List;

public interface SubmissionRepositoryCustom {

    List<SubmissionSummaryDto> findSubmissionSummariesByExamId(Long examId);

    List<MySubmissionSummaryDto> findMySubmissionSummaries(Long examId, Long memberId);
}
