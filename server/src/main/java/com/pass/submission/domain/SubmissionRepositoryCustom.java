package com.pass.submission.domain;

import com.pass.submission.domain.dto.SubmissionDetailDto;
import com.pass.submission.domain.dto.SubmissionSummaryDto;
import java.util.List;
import java.util.Optional;

public interface SubmissionRepositoryCustom {

    List<SubmissionSummaryDto> findSummariesByExamId(Long examId);

//    Optional<SubmissionDetailDto> findDetail(Long submissionId);
}
