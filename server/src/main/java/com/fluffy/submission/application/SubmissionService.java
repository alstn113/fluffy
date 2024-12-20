package com.fluffy.submission.application;

import com.fluffy.auth.domain.Member;
import com.fluffy.auth.domain.MemberRepository;
import com.fluffy.exam.domain.Exam;
import com.fluffy.exam.domain.ExamRepository;
import com.fluffy.global.exception.BadRequestException;
import com.fluffy.global.redis.DistributedLock;
import com.fluffy.submission.application.request.SubmissionAppRequest;
import com.fluffy.submission.domain.Submission;
import com.fluffy.submission.domain.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubmissionService {

    private final ExamRepository examRepository;
    private final SubmissionRepository submissionRepository;
    private final MemberRepository memberRepository;
    private final SubmissionMapper submissionMapper;

    @DistributedLock(key = "#lockName")
    public void submit(SubmissionAppRequest request, String lockName) {
        Member member = memberRepository.getById(request.accessor().id());
        Exam exam = examRepository.getById(request.examId());

        if (exam.isNotPublished()) {
            throw new BadRequestException("시험이 공개되지 않았습니다.");
        }

        if (submissionRepository.existsByExamIdAndMemberId(exam.getId(), member.getId())) {
            throw new BadRequestException("이미 제출한 시험입니다.");
        }

        Submission submission = submissionMapper.toSubmission(exam, member.getId(), request);
        submissionRepository.save(submission);
    }
}
