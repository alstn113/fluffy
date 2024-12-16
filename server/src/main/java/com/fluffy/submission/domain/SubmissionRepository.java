package com.fluffy.submission.domain;

import com.fluffy.global.exception.NotFoundException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface SubmissionRepository extends Repository<Submission, Long>, SubmissionRepositoryCustom {

    List<Submission> findAll();

    void save(Submission submission);

    boolean existsByExamIdAndMemberId(Long examId, Long memberId);

    Optional<Submission> findById(Long submissionId);

    default Submission getById(Long submissionId) {
        return findById(submissionId)
                .orElseThrow(() -> new NotFoundException("존재하지 않는 제출입니다. 제출 식별자: " + submissionId));
    }
}
