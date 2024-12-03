package com.pass.submission.application;

import com.pass.auth.domain.Member;
import com.pass.auth.domain.MemberRepository;
import com.pass.exam.domain.Exam;
import com.pass.exam.domain.ExamRepository;
import com.pass.global.exception.ForbiddenException;
import com.pass.global.web.Accessor;
import com.pass.submission.application.dto.SubmissionDetailResponse;
import com.pass.submission.domain.Submission;
import com.pass.submission.domain.SubmissionRepository;
import com.pass.submission.domain.dto.SubmissionSummaryDto;
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
        Exam exam = examRepository.getById(examId);

        if (exam.isNotWrittenBy(accessor.id())) {
            throw new ForbiddenException("해당 시험 제출 목록을 조회할 권한이 없습니다.");
        }

        return submissionRepository.findSummariesByExamId(examId);
    }

    @Transactional(readOnly = true)
    public SubmissionDetailResponse getDetail(Long examId, Long submissionId, Accessor accessor) {
        Exam exam = examRepository.getById(examId);
        Member member = memberRepository.getById(accessor.id());

        if (exam.isNotWrittenBy(member.getId())) {
            throw new ForbiddenException("해당 시험 제출을 조회할 권한이 없습니다.");
        }

        Submission submission = submissionRepository.getById(submissionId);

        return submissionMapper.toDetailResponse(exam, submission, member);
    }
}
