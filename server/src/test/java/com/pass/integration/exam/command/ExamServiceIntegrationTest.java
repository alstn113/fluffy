package com.pass.integration.exam.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.pass.auth.domain.Member;
import com.pass.auth.domain.MemberRepository;
import com.pass.exam.application.ExamService;
import com.pass.exam.application.dto.CreateExamAppRequest;
import com.pass.exam.application.dto.UpdateExamQuestionsAppRequest;
import com.pass.exam.application.dto.question.request.LongAnswerQuestionAppRequest;
import com.pass.exam.application.dto.question.request.MultipleChoiceAppRequest;
import com.pass.exam.application.dto.question.request.QuestionAppRequest;
import com.pass.exam.application.dto.question.request.QuestionOptionRequest;
import com.pass.exam.application.dto.question.request.ShortAnswerQuestionAppRequest;
import com.pass.exam.application.dto.question.request.SingleChoiceQuestionAppRequest;
import com.pass.exam.application.dto.question.request.TrueOrFalseQuestionAppRequest;
import com.pass.exam.application.dto.question.response.CreateExamResponse;
import com.pass.exam.domain.Exam;
import com.pass.exam.domain.ExamRepository;
import com.pass.exam.domain.QuestionRepository;
import com.pass.global.exception.ForbiddenException;
import com.pass.global.web.Accessor;
import com.pass.integration.AbstractIntegrationTest;
import com.pass.support.data.MemberTestData;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ExamServiceIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private ExamService examService;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("초기 시험을 생성할 수 있다.")
    void create() {
        // given
        Member member = memberRepository.save(MemberTestData.defaultMember().build());
        Accessor accessor = new Accessor(member.getId());
        CreateExamAppRequest request = new CreateExamAppRequest("시험 제목", accessor);

        // when
        CreateExamResponse response = examService.create(request);

        // then
        assertAll(
                () -> assertThat(response.id()).isNotNull(),
                () -> assertThat(response.title()).isEqualTo("시험 제목"),
                () -> assertThat(examRepository.findAll()).hasSize(1)
        );
    }

    @Test
    @DisplayName("시험에 질문들을 추가할 수 있다.")
    void updateQuestionGroup() {
        // given
        Member member = memberRepository.save(MemberTestData.defaultMember().build());
        Exam existingExam = examRepository.save(Exam.initial("시험 제목", member.getId()));

        Accessor accessor = new Accessor(member.getId());
        List<QuestionAppRequest> questionRequests = createQuestionRequests();
        UpdateExamQuestionsAppRequest request = new UpdateExamQuestionsAppRequest(
                existingExam.getId(), questionRequests, accessor);

        // when
        examService.updateQuestions(request);

        // then
        assertAll(
                () -> assertThat(questionRepository.findAll()).hasSize(5)
        );
    }

    @Test
    @DisplayName("내가 만든 시험이 아니면 시험에 질문을 추가할 수 없다.")
    void updateQuestionGroupFailWhenNotWrittenByMember() {
        // given
        Member member = memberRepository.save(MemberTestData.defaultMember().build());
        Exam existingExam = examRepository.save(Exam.initial("시험 제목", member.getId()));

        Member anotherMember = memberRepository.save(MemberTestData.defaultMember().build());
        Accessor accessor = new Accessor(anotherMember.getId());
        List<QuestionAppRequest> questionRequests = createQuestionRequests();
        UpdateExamQuestionsAppRequest request = new UpdateExamQuestionsAppRequest(existingExam.getId(),
                questionRequests, accessor);

        // when & then
        assertThatThrownBy(() -> examService.updateQuestions(request))
                .isInstanceOf(ForbiddenException.class);
    }

    private List<QuestionAppRequest> createQuestionRequests() {
        return List.of(
                new ShortAnswerQuestionAppRequest("주관식 질문", "SHORT_ANSWER", "주관식 질문 정답"),
                new LongAnswerQuestionAppRequest("서술형 질문", "LONG_ANSWER"),
                new SingleChoiceQuestionAppRequest(
                        "객관식 단일 선택 질문",
                        "SINGLE_CHOICE",
                        List.of(
                                new QuestionOptionRequest("객관식 질문 1", true),
                                new QuestionOptionRequest("객관식 질문 2", false)
                        )
                ),
                new MultipleChoiceAppRequest(
                        "객관식 다중 선택 질문",
                        "MULTIPLE_CHOICE",
                        List.of(
                                new QuestionOptionRequest("객관식 질문 1", true),
                                new QuestionOptionRequest("객관식 질문 2", false),
                                new QuestionOptionRequest("객관식 질문 3", true)
                        )
                ),
                new TrueOrFalseQuestionAppRequest(
                        "참/거짓 질문",
                        "TRUE_OR_FALSE",
                        false
                )
        );
    }
}
