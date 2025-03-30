package com.fluffy.exam.ui

import com.fluffy.support.AbstractDocumentTest
import io.mockk.every
import jakarta.servlet.http.Cookie
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class ExamLikeDocumentTest : AbstractDocumentTest() {

    @Test
    @DisplayName("시험에 좋아요를 할 수 있다.")
    fun like() {
        every { examLikeService.like(any(), any()) } returns 1L

        mockMvc.perform(
            post("/api/v1/exams/{examId}/like", 1)
                .cookie(Cookie("accessToken", "{ACCESS_TOKEN}"))
        )
            .andExpect(status().isOk())
            .andDo(
                restDocs.document(
                    pathParameters(
                        parameterWithName("examId").description("시험 ID")
                    )
                )
            )
    }

    @Test
    @DisplayName("시험에 좋아요를 취소할 수 있다.")
    fun unlike() {
        every { examLikeService.like(any(), any()) } returns 1L

        mockMvc.perform(
            post("/api/v1/exams/{examId}/like", 1)
                .cookie(Cookie("accessToken", "{ACCESS_TOKEN}"))
        )
            .andExpect(status().isOk())
            .andDo(
                restDocs.document(
                    pathParameters(
                        parameterWithName("examId").description("시험 ID")
                    )
                )
            )
    }
}