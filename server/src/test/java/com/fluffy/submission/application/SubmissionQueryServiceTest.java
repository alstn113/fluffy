package com.fluffy.submission.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.fluffy.auth.domain.Member;
import com.fluffy.auth.domain.MemberRepository;
import com.fluffy.exam.domain.Exam;
import com.fluffy.exam.domain.ExamRepository;
import com.fluffy.exam.domain.Question;
import com.fluffy.global.exception.ForbiddenException;
import com.fluffy.global.web.Accessor;
import com.fluffy.submission.application.response.SubmissionDetailResponse;
import com.fluffy.submission.application.response.SubmissionDetailResponse.TextAnswerResponse;
import com.fluffy.submission.domain.Answer;
import com.fluffy.submission.domain.Submission;
import com.fluffy.submission.domain.SubmissionRepository;
import com.fluffy.submission.domain.dto.SubmissionSummaryDto;
import com.fluffy.support.AbstractIntegrationTest;
import com.fluffy.support.data.MemberTestData;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class SubmissionQueryServiceTest extends AbstractIntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private SubmissionQueryService submissionQueryService;

    @Test
    @DisplayName("시험에 대한 제출 요약 목록을 조회할 수 있다.")
    void getSummariesByExamId() {
        // given
        Member author = MemberTestData.defaultMember().build();
        memberRepository.save(author);

        Exam exam = Exam.create("title", author.getId());
        exam.updateQuestions(List.of(Question.shortAnswer("질문", "지문", "답")));
        exam.publish(null, null);
        examRepository.save(exam);

        Member submitter1 = MemberTestData.defaultMember().build();
        memberRepository.save(submitter1);

        Submission submission1 = new Submission(
                exam.getId(),
                submitter1.getId(),
                List.of(Answer.textAnswer(1L, "답"))
        );
        submissionRepository.save(submission1);

        Member submitter2 = MemberTestData.defaultMember().build();
        memberRepository.save(submitter2);

        Submission submission2 = new Submission(
                exam.getId(),
                submitter2.getId(),
                List.of(Answer.textAnswer(1L, "답"))
        );
        submissionRepository.save(submission2);

        // when
        List<SubmissionSummaryDto> summaries = submissionQueryService
                .getSummariesByExamId(exam.getId(), new Accessor(author.getId()));

        // then
        assertAll(
                () -> assertThat(summaries).hasSize(2),
                () -> assertThat(summaries.stream().map(SubmissionSummaryDto::getId))
                        .containsExactlyElementsOf(List.of(submission2.getId(), submission1.getId()))
        );
    }

    @Test
    @DisplayName("시험에 대한 제출 요약 목록 조회 시 작성자가 아닌 경우 예외를 발생시킨다.")
    void getSummariesByExamIdFailWhenNotAuthor() {
        // given
        Member author = MemberTestData.defaultMember().build();
        memberRepository.save(author);

        Exam exam = Exam.create("title", author.getId());
        exam.updateQuestions(List.of(Question.shortAnswer("질문", "지문", "답")));
        exam.publish(null, null);
        examRepository.save(exam);

        // when
        Accessor notAuthor = new Accessor(-1L);

        // when & then
        assertThatThrownBy(() -> submissionQueryService.getSummariesByExamId(exam.getId(), notAuthor))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage("해당 시험 제출 목록을 조회할 권한이 없습니다.");
    }

    @Test
    @DisplayName("시험에 대한 제출 상세와 답안을 조회할 수 있다.")
    void getDetail() {
        // given
        Member author = MemberTestData.defaultMember().build();
        memberRepository.save(author);

        Exam exam = Exam.create("title", author.getId());
        exam.updateQuestions(List.of(Question.shortAnswer("질문", "지문", "질문 답")));
        exam.publish(null, null);
        examRepository.save(exam);

        Member submitter = MemberTestData.defaultMember().build();
        memberRepository.save(submitter);

        Submission submission = new Submission(
                exam.getId(),
                submitter.getId(),
                List.of(Answer.textAnswer(1L, "나의 답"))
        );
        submissionRepository.save(submission);

        // when
        SubmissionDetailResponse detail = submissionQueryService
                .getDetail(exam.getId(), submission.getId(), new Accessor(author.getId()));

        TextAnswerResponse textAnswer = (TextAnswerResponse) detail.answers().getFirst();

        // then
        assertAll(
                () -> assertThat(detail.participant().getId()).isEqualTo(submission.getMemberId()),
                () -> assertThat(textAnswer.questionId()).isEqualTo(1L),
                () -> assertThat(textAnswer.type()).isEqualTo("SHORT_ANSWER"),
                () -> assertThat(textAnswer.text()).isEqualTo("질문"),
                () -> assertThat(textAnswer.answer()).isEqualTo("나의 답"),
                () -> assertThat(textAnswer.correctAnswer()).isEqualTo("질문 답")
        );
    }

    @Test
    @DisplayName("시험에 대한 제출 상세와 답안 조회 시 작성자가 아닌 경우 예외를 발생시킨다.")
    void getDetailFailWhenNotAuthor() {
        // given
        Member author = MemberTestData.defaultMember().build();
        memberRepository.save(author);

        Exam exam = Exam.create("title", author.getId());
        exam.updateQuestions(List.of(Question.shortAnswer("질문", "지문", "질문 답")));
        exam.publish(null, null);
        examRepository.save(exam);

        Member submitter = MemberTestData.defaultMember().build();
        memberRepository.save(submitter);

        Submission submission = new Submission(
                exam.getId(),
                submitter.getId(),
                List.of(Answer.textAnswer(1L, "나의 답"))
        );
        submissionRepository.save(submission);

        // when
        Accessor notAuthor = new Accessor(-1L);

        // then
        assertThatThrownBy(() -> submissionQueryService.getDetail(exam.getId(), submission.getId(), notAuthor))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage("해당 시험 제출을 조회할 권한이 없습니다.");
    }
}
