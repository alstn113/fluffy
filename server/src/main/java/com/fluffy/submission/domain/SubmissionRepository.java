package com.fluffy.submission.domain;

import com.fluffy.global.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubmissionRepository extends JpaRepository<Submission, Long>, SubmissionRepositoryCustom {

    boolean existsByExamIdAndMemberId(Long examId, Long memberId);

    default Submission findByIdOrThrow(Long submissionId) {
        return findById(submissionId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 제출입니다. 제출 식별자: " + submissionId));
    }
}
