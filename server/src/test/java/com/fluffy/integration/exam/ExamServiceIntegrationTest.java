package com.fluffy.integration.exam;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.fluffy.auth.domain.Member;
import com.fluffy.auth.domain.MemberRepository;
import com.fluffy.exam.application.ExamService;
import com.fluffy.exam.application.request.CreateExamAppRequest;
import com.fluffy.exam.application.request.UpdateExamQuestionsAppRequest;
import com.fluffy.exam.application.response.CreateExamResponse;
import com.fluffy.exam.application.request.question.LongAnswerQuestionAppRequest;
import com.fluffy.exam.application.request.question.MultipleChoiceAppRequest;
import com.fluffy.exam.application.request.question.QuestionAppRequest;
import com.fluffy.exam.application.request.question.QuestionOptionRequest;
import com.fluffy.exam.application.request.question.ShortAnswerQuestionAppRequest;
import com.fluffy.exam.application.request.question.SingleChoiceQuestionAppRequest;
import com.fluffy.exam.application.request.question.TrueOrFalseQuestionAppRequest;
import com.fluffy.exam.domain.Exam;
import com.fluffy.exam.domain.ExamRepository;
import com.fluffy.exam.domain.QuestionRepository;
import com.fluffy.global.exception.ForbiddenException;
import com.fluffy.global.web.Accessor;
import com.fluffy.integration.AbstractIntegrationTest;
import com.fluffy.support.data.MemberTestData;
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
    void updateQuestions() {
        // given
        Member member = memberRepository.save(MemberTestData.defaultMember().build());
        Exam existingExam = examRepository.save(Exam.create("시험 제목", member.getId()));

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
    void updateQuestionsFailWhenNotWrittenByMember() {
        // given
        Member member = memberRepository.save(MemberTestData.defaultMember().build());
        Exam existingExam = examRepository.save(Exam.create("시험 제목", member.getId()));

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
