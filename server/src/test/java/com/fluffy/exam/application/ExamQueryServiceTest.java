package com.fluffy.exam.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.fluffy.auth.domain.Member;
import com.fluffy.auth.domain.MemberRepository;
import com.fluffy.exam.application.response.ExamDetailResponse;
import com.fluffy.exam.application.response.ExamDetailResponse.AnswerQuestionResponse;
import com.fluffy.exam.application.response.ExamWithAnswersResponse;
import com.fluffy.exam.application.response.ExamWithAnswersResponse.AnswerQuestionWithAnswersResponse;
import com.fluffy.exam.domain.Exam;
import com.fluffy.exam.domain.ExamRepository;
import com.fluffy.exam.domain.ExamStatus;
import com.fluffy.exam.domain.Question;
import com.fluffy.exam.domain.dto.ExamSummaryDto;
import com.fluffy.exam.domain.dto.MyExamSummaryDto;
import com.fluffy.global.exception.ForbiddenException;
import com.fluffy.global.response.PageInfo;
import com.fluffy.global.response.PageResponse;
import com.fluffy.global.web.Accessor;
import com.fluffy.support.AbstractIntegrationTest;
import com.fluffy.support.data.MemberTestData;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class ExamQueryServiceTest extends AbstractIntegrationTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private ExamQueryService examQueryService;

    @Test
    @DisplayName("출제된 시험 요약 목록을 조회할 수 있다.")
    void getPublishedExamSummaries() {
        // given
        Member author = MemberTestData.defaultMember().build();
        memberRepository.save(author);

        Exam exam1 = Exam.create("시험 제목1", author.getId());
        exam1.updateQuestions(List.of(Question.shortAnswer("질문1", "지문", "답1")));
        exam1.publish();
        examRepository.save(exam1);

        Exam exam2 = Exam.create("시험 제목2", author.getId());
        exam2.updateQuestions(List.of(Question.shortAnswer("질문2", "지문", "답2")));
        exam2.publish();
        examRepository.save(exam2);

        Exam exam3 = Exam.create("시험 제목3", author.getId());
        exam3.updateQuestions(List.of(Question.shortAnswer("질문3", "지문", "답3")));
        exam3.publish();
        examRepository.save(exam3);

        // when
        Pageable pageable = PageRequest.of(0, 2);
        PageResponse<ExamSummaryDto> summaries = examQueryService.getPublishedExamSummaries(pageable);

        // then
        assertAll(
                () -> assertThat(summaries.content()).hasSize(2),
                () -> assertThat(summaries.pageInfo()).isEqualTo(new PageInfo(0, 2, 3, true, false)),
                () -> assertThat(summaries.content().stream().map(ExamSummaryDto::getId))
                        .containsExactlyElementsOf(List.of(exam3.getId(), exam2.getId()))
        );
    }

    @Test
    @DisplayName("나의 시험 요약 목록을 조회할 수 있다.")
    void getMyExamSummaries() {
        // given
        Member author = MemberTestData.defaultMember().build();
        memberRepository.save(author);

        Member another = MemberTestData.defaultMember().build();
        memberRepository.save(another);

        Exam exam1 = Exam.create("시험 제목1", author.getId());
        exam1.updateQuestions(List.of(Question.shortAnswer("질문1", "지문", "답1")));
        exam1.publish();
        examRepository.save(exam1);

        Exam exam2 = Exam.create("시험 제목2", author.getId());
        exam2.updateQuestions(List.of(Question.shortAnswer("질문2", "지문", "답2")));
        exam2.publish();
        examRepository.save(exam2);

        Exam otherExam1 = Exam.create("시험 제목3", author.getId());
        otherExam1.updateQuestions(List.of(Question.shortAnswer("질문3", "지문", "답3")));
        examRepository.save(otherExam1);

        Exam exam4 = Exam.create("시험 제목4", another.getId());
        exam4.updateQuestions(List.of(Question.shortAnswer("질문4", "지문", "답4")));
        exam4.publish();
        examRepository.save(exam4);

        Exam exam5 = Exam.create("시험 제목5", author.getId());
        exam5.updateQuestions(List.of(Question.shortAnswer("질문5", "지문", "답5")));
        exam5.publish();
        examRepository.save(exam5);

        // when
        Pageable pageable = PageRequest.of(0, 2);
        PageResponse<MyExamSummaryDto> summaries = examQueryService
                .getMyExamSummaries(pageable, ExamStatus.PUBLISHED, new Accessor(author.getId()));

        // then
        assertAll(
                () -> assertThat(summaries.content()).hasSize(2),
                () -> assertThat(summaries.pageInfo()).isEqualTo(new PageInfo(0, 2, 3, true, false)),
                () -> assertThat(summaries.content().stream().map(MyExamSummaryDto::getId))
                        .containsExactlyElementsOf(List.of(exam5.getId(), exam2.getId()))
        );
    }

    @Test
    @DisplayName("시험 상세 정보를 조회할 수 있다.")
    void getExamDetail() {
        // given
        Member member = MemberTestData.defaultMember().build();
        memberRepository.save(member);

        Exam exam = Exam.create("시험 제목", member.getId());
        exam.updateQuestions(List.of(Question.shortAnswer("질문", "지문", "답")));
        exam.publish();
        examRepository.save(exam);

        // when
        ExamDetailResponse response = examQueryService.getExamDetail(exam.getId());
        AnswerQuestionResponse answerQuestion = (AnswerQuestionResponse) response.questions().getFirst();

        // then
        assertAll(
                () -> assertThat(response.id()).isEqualTo(exam.getId()),
                () -> assertThat(response.title()).isEqualTo(exam.getTitle()),
                () -> assertThat(response.status()).isEqualTo(exam.getStatus().name()),
                () -> assertThat(response.questions()).hasSize(1),
                () -> assertThat(answerQuestion.text()).isEqualTo("질문")
        );
    }

    @Test
    @DisplayName("시험 상세 정보를 답안과 함께 조회할 수 있다.")
    void getExamWithAnswers() {
        // given
        Member member = MemberTestData.defaultMember().build();
        memberRepository.save(member);

        Exam exam = Exam.create("시험 제목", member.getId());
        exam.updateQuestions(List.of(Question.shortAnswer("질문", "지문", "답")));
        exam.publish();
        examRepository.save(exam);

        // when
        ExamWithAnswersResponse response = examQueryService
                .getExamWithAnswers(exam.getId(), new Accessor(member.getId()));

        AnswerQuestionWithAnswersResponse answerQuestion =
                (AnswerQuestionWithAnswersResponse) response.questions().getFirst();

        // then
        assertAll(
                () -> assertThat(response.id()).isEqualTo(exam.getId()),
                () -> assertThat(response.title()).isEqualTo(exam.getTitle()),
                () -> assertThat(response.status()).isEqualTo(exam.getStatus().name()),
                () -> assertThat(response.questions()).hasSize(1),
                () -> assertThat(answerQuestion.text()).isEqualTo("질문"),
                () -> assertThat(answerQuestion.correctAnswer()).isEqualTo("답")
        );
    }

    @Test
    @DisplayName("시험 상세 정보를 답안과 함께 조회 시 작성자가 아닌 경우 예외를 발생시킨다.")
    void getExamWithAnswersFailWhenNotWrittenBy() {
        // given
        Member member = MemberTestData.defaultMember().build();
        memberRepository.save(member);

        Exam exam = Exam.create("시험 제목", member.getId());
        exam.updateQuestions(List.of(Question.shortAnswer("질문", "지문", "답")));
        exam.publish();
        examRepository.save(exam);

        // when
        Accessor notAuthor = new Accessor(-1L);

        // then
        assertThatThrownBy(() -> examQueryService.getExamWithAnswers(exam.getId(), notAuthor))
                .isInstanceOf(ForbiddenException.class)
                .hasMessage("해당 시험에 접근할 수 없습니다.");
    }
}
