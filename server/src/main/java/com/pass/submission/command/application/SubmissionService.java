package com.pass.submission.command.application;

import com.pass.auth.domain.Member;
import com.pass.auth.domain.MemberRepository;
import com.pass.exam.command.domain.Exam;
import com.pass.exam.command.domain.ExamRepository;
import com.pass.submission.command.application.dto.SubmitAppRequest;
import com.pass.submission.command.domain.Submission;
import com.pass.submission.command.domain.SubmissionRepository;
import com.pass.submission.command.domain.SubmissionValidator;
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
    private final SubmissionValidator submissionValidator;

    @Transactional
    public void submit(SubmitAppRequest request) {
        Member member = memberRepository.getById(request.accessor().id());
        Exam exam = examRepository.getById(request.examId());

        Submission submission = submissionMapper.toSubmission(exam.getId(), member.getId(), request);
        submissionValidator.validate(exam, submission);

        submissionRepository.save(submission);
    }
}
