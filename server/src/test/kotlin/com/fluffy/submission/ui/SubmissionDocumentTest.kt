package com.fluffy.submission.ui

import com.fluffy.submission.application.request.QuestionResponseRequest
import com.fluffy.submission.application.response.ChoiceAnswerResponse
import com.fluffy.submission.application.response.ChoiceResponse
import com.fluffy.submission.application.response.SubmissionDetailResponse
import com.fluffy.submission.application.response.TextAnswerResponse
import com.fluffy.submission.domain.dto.MySubmissionSummaryDto
import com.fluffy.submission.domain.dto.ParticipantDto
import com.fluffy.submission.domain.dto.SubmissionSummaryDto
import com.fluffy.submission.ui.request.SubmissionWebRequest
import com.fluffy.support.AbstractDocumentTest
import io.mockk.every
import io.mockk.verify
import jakarta.servlet.http.Cookie
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

class SubmissionDocumentTest : AbstractDocumentTest() {

    @Test
    @DisplayName("시험 제출 요약 목록을 조회할 수 있다.")
    fun getSummaries() {
        val response = listOf(
            SubmissionSummaryDto(
                1L,
                ParticipantDto(1L, "제출자1", "a@gmail.com", "https://a.com"),
                LocalDateTime.now()
            ),
            SubmissionSummaryDto(
                2L,
                ParticipantDto(2L, "제출자2", "b@gmail.com", "https://b.com"),
                LocalDateTime.now()
            )
        )

        every { submissionQueryService.getSummariesByExamId(any(), any()) } returns response

        mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/v1/exams/{examId}/submissions", 1)
                .cookie(Cookie("accessToken", "{ACCESS_TOKEN}"))
        )
            .andExpectAll(
                status().isOk,
                content().json(objectMapper.writeValueAsString(response))
            )
            .andDo(
                restDocs.document(
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
                )
            )
    }

    @Test
    @DisplayName("시험 제출 상세 정보를 조회할 수 있다.")
    fun getDetail() {
        val response = SubmissionDetailResponse(
            1L,
            listOf(
                TextAnswerResponse(1L, 1L, "질문", "SHORT_ANSWER", "답", "정답"),
                TextAnswerResponse(2L, 2L, "질문2", "SHORT_ANSWER", "답2", "정답2"),
                ChoiceAnswerResponse(
                    3L, 3L, "질문3", "MULTIPLE_CHOICE", listOf(
                        ChoiceResponse(1L, "선택지1", true, true),
                        ChoiceResponse(2L, "선택지2", false, false)
                    )
                ),
                ChoiceAnswerResponse(
                    4L, 4L, "질문4", "MULTIPLE_CHOICE", listOf(
                        ChoiceResponse(3L, "선택지1", true, true),
                        ChoiceResponse(4L, "선택지2", true, false)
                    )
                ),
                ChoiceAnswerResponse(
                    5L, 5L, "질문5", "SINGLE_CHOICE", listOf(
                        ChoiceResponse(5L, "선택지1", true, true),
                        ChoiceResponse(6L, "선택지2", false, false)
                    )
                )
            ),
            ParticipantDto(1L, "제출자", "a@gmail.com", "https://a.com"),
            LocalDateTime.now()
        )

        every { submissionQueryService.getDetail(any(), any(), any()) } returns response

        mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/v1/exams/{examId}/submissions/{submissionId}", 1, 1)
                .cookie(Cookie("accessToken", "{ACCESS_TOKEN}"))
        )
            .andExpectAll(
                status().isOk,
                content().json(objectMapper.writeValueAsString(response))
            )
            .andDo(
                restDocs.document(
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
                )
            )
    }

    @Test
    @DisplayName("나의 시험 제출 요약 목록을 조회할 수 있다.")
    fun getMySubmissionSummaries() {
        val response = listOf(
            MySubmissionSummaryDto(1L, LocalDateTime.now()),
            MySubmissionSummaryDto(2L, LocalDateTime.now())
        )

        every { submissionQueryService.getMySubmissionSummaries(any(), any()) } returns response

        mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/v1/exams/{examId}/submissions/me", 1)
                .cookie(Cookie("accessToken", "{ACCESS_TOKEN}"))
        )
            .andExpectAll(
                status().isOk,
                content().json(objectMapper.writeValueAsString(response))
            )
            .andDo(
                restDocs.document(
                    pathParameters(
                        parameterWithName("examId").description("시험 ID")
                    ),
                    responseFields(
                        fieldWithPath("[].submissionId").description("제출 ID"),
                        fieldWithPath("[].submittedAt").description("제출 시각")
                    )
                )
            )
    }

    @Test
    @DisplayName("시험 제출을 할 수 있다.")
    fun submit() {
        val request = SubmissionWebRequest(
            listOf(
                QuestionResponseRequest(listOf("답")),
                QuestionResponseRequest(listOf("닭은 동물입니다.", "닭은 곤충입니다."))
            )
        )

        every { submissionService.submit(any()) } returns Unit

        mockMvc.perform(
            RestDocumentationRequestBuilders.post("/api/v1/exams/{examId}/submissions", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .cookie(Cookie("accessToken", "{ACCESS_TOKEN}"))
        )
            .andExpectAll(status().isOk)
            .andDo(
                restDocs.document(
                    pathParameters(
                        parameterWithName("examId").description("시험 ID")
                    ),
                    requestFields(
                        fieldWithPath("questionResponses[].answers[]").description("답 또는 선택 옵션")
                    )
                )
            )

        verify { submissionService.submit(any()) }
    }
}
