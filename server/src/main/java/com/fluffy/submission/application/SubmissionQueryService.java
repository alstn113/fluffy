package com.fluffy.submission.application;

import com.fluffy.auth.domain.Member;
import com.fluffy.auth.domain.MemberRepository;
import com.fluffy.exam.domain.Exam;
import com.fluffy.exam.domain.ExamRepository;
import com.fluffy.global.exception.ForbiddenException;
import com.fluffy.global.web.Accessor;
import com.fluffy.submission.application.response.SubmissionDetailResponse;
import com.fluffy.submission.domain.Submission;
import com.fluffy.submission.domain.SubmissionRepository;
import com.fluffy.submission.domain.dto.SubmissionSummaryDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubmissionQueryService {

    private final ExamRepository examRepository;
    private final SubmissionRepository submissionRepository;
    private final MemberRepository memberRepository;
    private final SubmissionMapper submissionMapper;

    @Transactional(readOnly = true)
    public List<SubmissionSummaryDto> getSummariesByExamId(Long examId, Accessor accessor) {
        Exam exam = examRepository.findByIdOrThrow(examId);

        if (exam.isNotWrittenBy(accessor.id())) {
            throw new ForbiddenException("해당 시험 제출 목록을 조회할 권한이 없습니다.");
        }

        return submissionRepository.findSubmissionSummariesByExamId(examId);
    }

    @Transactional(readOnly = true)
    public SubmissionDetailResponse getDetail(Long examId, Long submissionId, Accessor accessor) {
        Exam exam = examRepository.findByIdOrThrow(examId);

        if (exam.isNotWrittenBy(accessor.id())) {
            throw new ForbiddenException("해당 시험 제출을 조회할 권한이 없습니다.");
        }

        Submission submission = submissionRepository.findByIdOrThrow(submissionId);
        Member submitter = memberRepository.findByIdOrThrow(submission.getMemberId());

        return submissionMapper.toDetailResponse(exam, submission, submitter);
    }
}
