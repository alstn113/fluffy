package com.pass.submission.application;

import com.pass.auth.domain.Member;
import com.pass.auth.domain.MemberRepository;
import com.pass.exam.domain.Exam;
import com.pass.exam.domain.ExamRepository;
import com.pass.submission.application.dto.SubmissionAppRequest;
import com.pass.submission.domain.Submission;
import com.pass.submission.domain.SubmissionRepository;
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

    @Transactional
    public void submit(SubmissionAppRequest request) {
        Member member = memberRepository.getById(request.accessor().id());
        Exam exam = examRepository.getById(request.examId());

        Submission submission = submissionMapper.toSubmission(exam, member.getId(), request);

        submissionRepository.save(submission);
    }
}
