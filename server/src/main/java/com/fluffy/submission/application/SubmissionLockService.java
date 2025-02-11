package com.fluffy.submission.application;

import com.fluffy.auth.domain.Member;
import com.fluffy.exam.domain.Exam;
import com.fluffy.global.exception.BadRequestException;
import com.fluffy.global.redis.distributedLock.DistributedLock;
import com.fluffy.submission.application.request.SubmissionAppRequest;
import com.fluffy.submission.domain.Submission;
import com.fluffy.submission.domain.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubmissionLockService {

    private final SubmissionRepository submissionRepository;
    private final SubmissionMapper submissionMapper;

    @DistributedLock(key = "#lockName")
    public void submitWithLock(SubmissionAppRequest request, Exam exam, Member member, String lockName) {
        if (submissionRepository.existsByExamIdAndMemberId(exam.getId(), member.getId())) {
            throw new BadRequestException("한 번만 제출 가능합니다.");
        }

        Submission submission = submissionMapper.toSubmission(exam, member.getId(), request);
        submissionRepository.save(submission);
    }
}
