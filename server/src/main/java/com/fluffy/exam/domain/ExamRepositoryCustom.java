package com.fluffy.exam.domain;

import com.fluffy.exam.domain.dto.ExamSummaryDto;
import com.fluffy.exam.domain.dto.MyExamSummaryDto;
import com.fluffy.exam.domain.dto.SubmittedExamSummaryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExamRepositoryCustom {

    Page<ExamSummaryDto> findPublishedExamSummaries(Pageable pageable);

    Page<MyExamSummaryDto> findMyExamSummaries(Pageable pageable, ExamStatus status, Long memberId);

    Page<SubmittedExamSummaryDto> findSubmittedExamSummaries(Pageable pageable, Long memberId);
}
