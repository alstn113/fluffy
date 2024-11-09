package com.pass.submission.command.domain;

import org.springframework.data.repository.Repository;

public interface SubmissionRepository extends Repository<Submission, Long> {

    Submission save(Submission submission);

    Submission findById(Long id);
}
