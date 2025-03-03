package com.fluffy.submission.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.fluffy.auth.domain.Member;
import com.fluffy.auth.domain.MemberRepository;
import com.fluffy.exam.domain.Exam;
import com.fluffy.exam.domain.ExamRepository;
import com.fluffy.exam.domain.Question;
import com.fluffy.submission.domain.dto.ParticipantDto;
import com.fluffy.submission.domain.dto.SubmissionSummaryDto;
import com.fluffy.support.AbstractIntegrationTest;
import com.fluffy.support.data.MemberTestData;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class SubmissionRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Test
    @DisplayName("시험에 대한 제출 요약 목록을 조회할 수 있다.")
    void findSubmissionSummariesByExamId() {
        // given
        Member author = MemberTestData.defaultMember().build();
        memberRepository.save(author);

        Exam exam = Exam.create("exam", author.getId());
        exam.updateQuestions(List.of(Question.shortAnswer("질문1", "지문", "답1")));
        exam.publish();
        examRepository.save(exam);

        Exam otherExam = Exam.create("otherExam", author.getId());
        otherExam.updateQuestions(List.of(Question.shortAnswer("질문2", "지문", "답2")));
        otherExam.publish();
        examRepository.save(otherExam);

        Member member1 = MemberTestData.defaultMember().build();
        memberRepository.save(member1);

        Member member2 = MemberTestData.defaultMember().build();
        memberRepository.save(member2);

        Submission member1Submission = new Submission(
                exam.getId(),
                member1.getId(),
                List.of(Answer.textAnswer(1L, "답1"))
        );
        submissionRepository.save(member1Submission);

        Submission otherExamSubmission = new Submission(
                otherExam.getId(),
                member1.getId(),
                List.of(Answer.textAnswer(1L, "답1"))
        );
        submissionRepository.save(otherExamSubmission);

        Submission member2Submission = new Submission(
                exam.getId(),
                member2.getId(),
                List.of(Answer.textAnswer(1L, "답2"))
        );
        submissionRepository.save(member2Submission);

        // when
        List<SubmissionSummaryDto> submissionSummaries = submissionRepository
                .findSubmissionSummariesByExamId(exam.getId());

        // then
        assertAll(
                () -> assertThat(submissionSummaries.size()).isEqualTo(2),
                () -> assertThat(submissionSummaries.stream()
                        .map(SubmissionSummaryDto::id))
                        .containsExactlyElementsOf(List.of(member2Submission.getId(), member1Submission.getId())),
                () -> assertThat(submissionSummaries.stream()
                        .map(SubmissionSummaryDto::participant)
                        .map(ParticipantDto::id))
                        .containsExactlyElementsOf(List.of(member2.getId(), member1.getId()))
        );
    }
}
