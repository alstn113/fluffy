package com.fluffy.submission.application;

import com.fluffy.auth.domain.Member;
import com.fluffy.auth.domain.MemberRepository;
import com.fluffy.exam.domain.Exam;
import com.fluffy.exam.domain.ExamRepository;
import com.fluffy.global.exception.BadRequestException;
import com.fluffy.submission.application.dto.SubmissionAppRequest;
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

    @Transactional
    public void submit(SubmissionAppRequest request) {
        Member member = memberRepository.getById(request.accessor().id());
        Exam exam = examRepository.getById(request.examId());

        if (exam.isNotPublished()) {
            throw new BadRequestException("시험이 공개되지 않았습니다.");
        }

        //TODO: 존재하지 않는 것에 대한 동시성 문제 aka 따닥 -> Redis 분산락 고려 ?
        //TODO: unique 제약조건 examId, memberId ?
        if (submissionRepository.existsByExamIdAndMemberId(exam.getId(), member.getId())) {
            throw new BadRequestException("이미 제출한 시험입니다.");
        }

        Submission submission = submissionMapper.toSubmission(exam, member.getId(), request);
        submissionRepository.save(submission);
    }
}
