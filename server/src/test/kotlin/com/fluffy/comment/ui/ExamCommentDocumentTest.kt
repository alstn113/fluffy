package com.fluffy.comment.ui

import com.fluffy.comment.application.response.AuthorResponse
import com.fluffy.comment.application.response.CreateExamCommentResponse
import com.fluffy.comment.domain.dto.AuthorDto
import com.fluffy.comment.domain.dto.ExamReplyCommentDto
import com.fluffy.comment.domain.dto.ExamRootCommentDto
import com.fluffy.comment.ui.request.CreateExamCommentWebRequest
import com.fluffy.comment.ui.response.DeleteExamCommentWebResponse
import com.fluffy.support.AbstractDocumentTest
import io.mockk.every
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

class ExamCommentDocumentTest : AbstractDocumentTest() {

    @Test
    @DisplayName("해당 시험에 대한 루트 댓글들을 조회할 수 있다.")
    fun getRootComments() {
        every { examCommentQueryService.getRootComments(any()) } returns listOf(
            ExamRootCommentDto(
                1L,
                "댓글",
                AuthorDto(1L, "사람", "https://image.com"),
                0L,
                false,
                LocalDateTime.now(),
                LocalDateTime.now()
            ),
            ExamRootCommentDto(
                2L,
                "",
                AuthorDto(-1L, "", ""),
                1L,
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
            )
        )

        mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/v1/exams/{examId}/comments", 1L)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpectAll(
                status().isOk
            )
            .andDo(
                restDocs.document(
                    pathParameters(
                        parameterWithName("examId").description("시험 식별자")
                    ),
                    responseFields(
                        fieldWithPath("comments.[].id").description("댓글 식별자"),
                        fieldWithPath("comments.[].content").description("댓글 내용"),
                        fieldWithPath("comments.[].author.id").description("작성자 식별자"),
                        fieldWithPath("comments.[].author.name").description("작성자 이름"),
                        fieldWithPath("comments.[].author.avatarUrl").description("작성자 프로필 이미지 URL"),
                        fieldWithPath("comments.[].replyCount").description("답글 수"),
                        fieldWithPath("comments.[].isDeleted").description("삭제 여부"),
                        fieldWithPath("comments.[].createdAt").description("댓글 작성 시각"),
                        fieldWithPath("comments.[].updatedAt").description("댓글 수정 시각")
                    )
                )
            )
    }

    @Test
    @DisplayName("시험에서 댓글에 대한 답글들을 조회할 수 있다.")
    fun getReplyComments() {
        every { examCommentQueryService.getReplyComments(any()) } returns listOf(
            ExamReplyCommentDto(
                1L,
                "답글",
                AuthorDto(1L, "사람", "https://image.com"),
                LocalDateTime.now(),
                LocalDateTime.now()
            )
        )

        mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/v1/exams/comments/{commentId}/replies", 1L)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpectAll(
                status().isOk
            )
            .andDo(
                restDocs.document(
                    pathParameters(
                        parameterWithName("commentId").description("댓글 식별자")
                    ),
                    responseFields(
                        fieldWithPath("replies.[].id").description("댓글 식별자"),
                        fieldWithPath("replies.[].content").description("댓글 내용"),
                        fieldWithPath("replies.[].author.id").description("작성자 식별자"),
                        fieldWithPath("replies.[].author.name").description("작성자 이름"),
                        fieldWithPath("replies.[].author.avatarUrl").description("작성자 프로필 이미지 URL"),
                        fieldWithPath("replies.[].createdAt").description("댓글 작성 시각"),
                        fieldWithPath("replies.[].updatedAt").description("댓글 수정 시각")
                    )
                )
            )
    }

    @Test
    @DisplayName("댓글을 작성할 수 있다.")
    fun createRootComment() {
        val request = CreateExamCommentWebRequest("댓글", null)
        val response = CreateExamCommentResponse(
            1L,
            "댓글",
            1L,
            null,
            AuthorResponse(1L, "사람", "https://image.com"),
            LocalDateTime.now()
        )

        every { examCommentService.createComment(any()) } returns response

        mockMvc.perform(
            RestDocumentationRequestBuilders.post("/api/v1/exams/{examId}/comments", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(Cookie("access_token", "{ACCESS_TOKEN}"))
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpectAll(
                status().isOk,
                content().json(objectMapper.writeValueAsString(response))
            )
            .andDo(
                restDocs.document(
                    pathParameters(
                        parameterWithName("examId").description("시험 식별자")
                    ),
                    requestFields(
                        fieldWithPath("content").description("댓글 내용"),
                        fieldWithPath("parentCommentId").description("부모 댓글 식별자")
                    ),
                    responseFields(
                        fieldWithPath("id").description("댓글 식별자"),
                        fieldWithPath("content").description("댓글 내용"),
                        fieldWithPath("examId").description("시험 식별자"),
                        fieldWithPath("parentCommentId").description("부모 댓글 식별자").optional(),
                        fieldWithPath("author.id").description("작성자 식별자"),
                        fieldWithPath("author.name").description("작성자 이름"),
                        fieldWithPath("author.avatarUrl").description("작성자 프로필 이미지 URL"),
                        fieldWithPath("createdAt").description("댓글 작성 시각")
                    )
                )
            )
    }

    @Test
    @DisplayName("답글을 작성할 수 있다.")
    fun createReplyComment() {
        val request = CreateExamCommentWebRequest("댓글", null)
        val response = CreateExamCommentResponse(
            2L,
            "답글",
            1L,
            1L,
            AuthorResponse(1L, "사람", "https://image.com"),
            LocalDateTime.now()
        )

        every { examCommentService.createComment(any()) } returns response

        mockMvc.perform(
            RestDocumentationRequestBuilders.post("/api/v1/exams/{examId}/comments", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(Cookie("access_token", "{ACCESS_TOKEN}"))
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpectAll(
                status().isOk,
                content().json(objectMapper.writeValueAsString(response))
            )
            .andDo(
                restDocs.document(
                    pathParameters(
                        parameterWithName("examId").description("시험 식별자")
                    ),
                    requestFields(
                        fieldWithPath("content").description("답글 내용"),
                        fieldWithPath("parentCommentId").description("부모 댓글 식별자")
                    ),
                    responseFields(
                        fieldWithPath("id").description("댓글 식별자"),
                        fieldWithPath("content").description("답글 내용"),
                        fieldWithPath("examId").description("시험 식별자"),
                        fieldWithPath("parentCommentId").description("부모 댓글 식별자").optional(),
                        fieldWithPath("author.id").description("작성자 식별자"),
                        fieldWithPath("author.name").description("작성자 이름"),
                        fieldWithPath("author.avatarUrl").description("작성자 프로필 이미지 URL"),
                        fieldWithPath("createdAt").description("답글 작성 시각")
                    )
                )
            )
    }

    @Test
    @DisplayName("댓글을 삭제할 수 있다.")
    fun deleteComment() {
        val response = DeleteExamCommentWebResponse(1L)

        every { examCommentService.deleteComment(any()) } returns 1L

        mockMvc.perform(
            RestDocumentationRequestBuilders.delete("/api/v1/exams/comments/{commentId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(Cookie("access_token", "{ACCESS_TOKEN}"))
        )
            .andExpectAll(
                status().isOk,
                content().json(objectMapper.writeValueAsString(response))
            )
            .andDo(
                restDocs.document(
                    pathParameters(
                        parameterWithName("commentId").description("댓글 식별자")
                    ),
                    responseFields(
                        fieldWithPath("id").description("삭제된 댓글 식별자")
                    )
                )
            )
    }
}
