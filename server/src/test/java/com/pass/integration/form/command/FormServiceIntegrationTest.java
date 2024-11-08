package com.pass.integration.form.command;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.pass.form.command.application.FormService;
import com.pass.form.command.application.dto.PublishFormAppRequest;
import com.pass.form.command.application.dto.CreateFormAppRequest;
import com.pass.form.command.application.dto.CreateFormResponse;
import com.pass.form.command.application.dto.question.LongAnswerQuestionAppRequest;
import com.pass.form.command.application.dto.question.MultipleChoiceAppRequest;
import com.pass.form.command.application.dto.question.QuestionAppRequest;
import com.pass.form.command.application.dto.question.ShortAnswerQuestionAppRequest;
import com.pass.form.command.application.dto.question.SingleChoiceQuestionAppRequest;
import com.pass.form.command.application.dto.question.TrueOrFalseQuestionAppRequest;
import com.pass.form.command.domain.Form;
import com.pass.form.command.domain.FormRepository;
import com.pass.form.command.domain.QuestionRepository;
import com.pass.integration.AbstractIntegrationTest;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class FormServiceIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private FormService formService;

    @Autowired
    private FormRepository formRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    @DisplayName("초기 폼을 생성할 수 있다.")
    void create() {
        // given
        CreateFormAppRequest request = new CreateFormAppRequest("폼 제목");

        // when
        CreateFormResponse response = formService.create(request);

        // then
        assertAll(
                () -> assertThat(response.id()).isNotNull(),
                () -> assertThat(response.title()).isEqualTo("폼 제목"),
                () -> assertThat(formRepository.findAll()).hasSize(1)
        );
    }

    @Test
    @DisplayName("폼에 질문들을 추가할 수 있다.")
    void addQuestions() {
        // given
        Form existingForm = formRepository.save(Form.initial("폼 제목"));
        List<QuestionAppRequest> questionRequests = createQuestionRequests();
        PublishFormAppRequest request = new PublishFormAppRequest(existingForm.getId(), questionRequests);

        // when
        formService.publish(request);

        // then
        assertAll(
                () -> assertThat(questionRepository.findAll()).hasSize(5)
        );
    }

    private List<QuestionAppRequest> createQuestionRequests() {
        return List.of(
                new ShortAnswerQuestionAppRequest("주관식 질문", "SHORT_ANSWER", "주관식 질문 정답"),
                new LongAnswerQuestionAppRequest("서술형 질문", "LONG_ANSWER"),
                new SingleChoiceQuestionAppRequest(
                        "객관식 단일 선택 질문",
                        "SINGLE_CHOICE",
                        List.of("객관식 질문 1", "객관식 질문 2"),
                        1
                ),
                new MultipleChoiceAppRequest(
                        "객관식 다중 선택 질문",
                        "MULTIPLE_CHOICE",
                        List.of("객관식 질문 1", "객관식 질문 2", "객관식 질문 3"),
                        List.of(1, 3)
                ),
                new TrueOrFalseQuestionAppRequest(
                        "참/거짓 질문",
                        "TRUE_OR_FALSE",
                        "참 질문",
                        "거짓 질문",
                        false
                )
        );
    }
}
