package com.fluffy.submission.application;

import com.fluffy.auth.domain.Member;
import com.fluffy.auth.domain.MemberRepository;
import com.fluffy.exam.domain.Exam;
import com.fluffy.exam.domain.ExamRepository;
import com.fluffy.global.exception.BadRequestException;
import com.fluffy.submission.application.request.SubmissionAppRequest;
import com.fluffy.submission.domain.Submission;
import com.fluffy.submission.domain.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubmissionService {

    private final ExamRepository examRepository;
    private final SubmissionRepository submissionRepository;
    private final MemberRepository memberRepository;
    private final SubmissionMapper submissionMapper;
    private final SubmissionLockService submissionLockService;

    @Transactional
    public void submit(SubmissionAppRequest request) {
        Member member = memberRepository.findByIdOrThrow(request.accessor().id());
        Exam exam = examRepository.findByIdOrThrow(request.examId());

        if (exam.isNotPublished()) {
            throw new BadRequestException("시험이 공개되지 않았습니다.");
        }

        if (exam.isSingleAttempt()) {
            String lockName = "submit:%d:%d".formatted(exam.getId(), member.getId());
            submissionLockService.submitWithLock(request, exam, member, lockName);
            return;
        }

        Submission submission = submissionMapper.toSubmission(exam, member.getId(), request);
        submissionRepository.save(submission);
    }
}
