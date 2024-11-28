package com.pass.submission.domain;

import org.springframework.data.repository.Repository;

public interface SubmissionRepository extends Repository<Submission, Long>, SubmissionRepositoryCustom {

    void save(Submission submission);
}
