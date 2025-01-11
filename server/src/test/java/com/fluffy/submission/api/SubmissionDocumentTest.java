package com.fluffy.submission.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fluffy.submission.api.request.SubmissionWebRequest;
import com.fluffy.submission.application.request.QuestionResponseAppRequest;
import com.fluffy.submission.application.response.SubmissionDetailResponse;
import com.fluffy.submission.application.response.SubmissionDetailResponse.ChoiceAnswerResponse;
import com.fluffy.submission.application.response.SubmissionDetailResponse.ChoiceResponse;
import com.fluffy.submission.application.response.SubmissionDetailResponse.TextAnswerResponse;
import com.fluffy.submission.domain.dto.MySubmissionSummaryDto;
import com.fluffy.submission.domain.dto.ParticipantDto;
import com.fluffy.submission.domain.dto.SubmissionSummaryDto;
import com.fluffy.support.AbstractDocumentTest;
import jakarta.servlet.http.Cookie;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

class SubmissionDocumentTest extends AbstractDocumentTest {

    @Test
    @DisplayName("시험 제출 요약 목록을 조회할 수 있다.")
    void getSummaries() throws Exception {
        List<SubmissionSummaryDto> response = List.of(
                new SubmissionSummaryDto(1L,
                        new ParticipantDto(1L, "제출자1", "a@gmail.com", "https://a.com"),
                        LocalDateTime.now()),
                new SubmissionSummaryDto(2L,
                        new ParticipantDto(2L, "제출자2", "b@gmail.com", "https://b.com"),
                        LocalDateTime.now())
        );

        when(submissionQueryService.getSummariesByExamId(any(), any()))
                .thenReturn(response);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/exams/{examId}/submissions", 1)
                        .cookie(new Cookie("accessToken", "{ACCESS_TOKEN}")))
                .andExpectAll(
                        status().isOk(),
                        content().json(objectMapper.writeValueAsString(response))
                )
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("examId").description("시험 ID")
                        ),
                        responseFields(
                                fieldWithPath("[].id").description("제출 ID"),
                                fieldWithPath("[].participant.id").description("제출자 ID"),
                                fieldWithPath("[].participant.name").description("제출자 이름"),
                                fieldWithPath("[].participant.email").description("제출자 이메일"),
                                fieldWithPath("[].participant.avatarUrl").description("제출자 프로필 URL"),
                                fieldWithPath("[].submittedAt").description("제출 시각")
                        )
                ));
    }

    @Test
    @DisplayName("시험 제출 상세 정보를 조회할 수 있다.")
    void getDetail() throws Exception {
        SubmissionDetailResponse response = new SubmissionDetailResponse(1L,
                List.of(
                        new TextAnswerResponse(1L, 1L, "질문", "SHORT_ANSWER", "답", "정답"),
                        new TextAnswerResponse(2L, 2L, "질문2", "SHORT_ANSWER", "답2", "정답2"),
                        new ChoiceAnswerResponse(3L, 3L, "질문3", "MULTIPLE_CHOICE", List.of(
                                new ChoiceResponse(1L, "선택지1", true, true),
                                new ChoiceResponse(2L, "선택지2", false, false)
                        )),
                        new ChoiceAnswerResponse(4L, 4L, "질문4", "MULTIPLE_CHOICE", List.of(
                                new ChoiceResponse(3L, "선택지1", true, true),
                                new ChoiceResponse(4L, "선택지2", true, false)
                        )),
                        new ChoiceAnswerResponse(5L, 5L, "질문5", "SINGLE_CHOICE", List.of(
                                new ChoiceResponse(5L, "선택지1", true, true),
                                new ChoiceResponse(6L, "선택지2", false, false)
                        ))
                ),
                new ParticipantDto(1L, "제출자", "a@gmail.com", "https://a.com"),
                LocalDateTime.now()
        );

        when(submissionQueryService.getDetail(any(), any(), any()))
                .thenReturn(response);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/exams/{examId}/submissions/{submissionId}", 1, 1)
                        .cookie(new Cookie("accessToken", "{ACCESS_TOKEN}"))
                )
                .andExpectAll(
                        status().isOk(),
                        content().json(objectMapper.writeValueAsString(response))
                )
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("examId").description("시험 ID"),
                                parameterWithName("submissionId").description("제출 ID")
                        ),
                        responseFields(
                                fieldWithPath("id").description("제출 ID"),
                                fieldWithPath("answers[].id").description("답안 ID"),
                                fieldWithPath("answers[].questionId").description("질문 ID"),
                                fieldWithPath("answers[].text").description("질문"),
                                fieldWithPath("answers[].type").description("질문 유형"),
                                fieldWithPath("answers[].answer").description("제출한 답").optional(),
                                fieldWithPath("answers[].correctAnswer").description("정답").optional(),
                                fieldWithPath("answers[].choices[].questionOptionId").description("선택지 ID").optional(),
                                fieldWithPath("answers[].choices[].text").description("선택지").optional(),
                                fieldWithPath("answers[].choices[].isCorrect").description("정답 여부").optional(),
                                fieldWithPath("answers[].choices[].isSelected").description("선택 여부").optional(),
                                fieldWithPath("participant.id").description("제출자 ID"),
                                fieldWithPath("participant.name").description("제출자 이름"),
                                fieldWithPath("participant.email").description("제출자 이메일"),
                                fieldWithPath("participant.avatarUrl").description("제출자 프로필 URL"),
                                fieldWithPath("submittedAt").description("제출 시각")
                        )
                ));
    }

    @Test
    @DisplayName("나의 시험 제출 요약 목록을 조회할 수 있다.")
    void getMySubmissionSummaries() throws Exception {
        List<MySubmissionSummaryDto> response = List.of(
                new MySubmissionSummaryDto(1L, LocalDateTime.now()),
                new MySubmissionSummaryDto(2L, LocalDateTime.now())
        );

        when(submissionQueryService.getMySubmissionSummaries(any(), any()))
                .thenReturn(response);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/exams/{examId}/submissions/me", 1)
                        .cookie(new Cookie("accessToken", "{ACCESS_TOKEN}"))
                )
                .andExpectAll(
                        status().isOk(),
                        content().json(objectMapper.writeValueAsString(response))
                )
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("examId").description("시험 ID")
                        ),
                        responseFields(
                                fieldWithPath("[].submissionId").description("제출 ID"),
                                fieldWithPath("[].submittedAt").description("제출 시각")
                        )
                ));
    }

    @Test
    @DisplayName("시험 제출을 할 수 있다.")
    void submit() throws Exception {
        SubmissionWebRequest request = new SubmissionWebRequest(List.of(
                new QuestionResponseAppRequest(List.of("답")),
                new QuestionResponseAppRequest(List.of("닭은 동물입니다.", "닭은 곤충입니다."))
        ));

        doNothing().when(submissionService).submit(any());

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/exams/{examId}/submissions", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .cookie(new Cookie("accessToken", "{ACCESS_TOKEN}"))
                )
                .andExpectAll(
                        status().isOk()
                )
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("examId").description("시험 ID")
                        ),
                        requestFields(
                                fieldWithPath("questionResponses[].answers[]").description("답 또는 선택 옵션")
                        )
                ));
    }
}
