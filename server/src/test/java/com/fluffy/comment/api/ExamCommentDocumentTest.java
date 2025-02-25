package com.fluffy.comment.api;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fluffy.comment.api.dto.CreateExamCommentWebRequest;
import com.fluffy.comment.api.dto.DeleteExamCommentWebResponse;
import com.fluffy.comment.application.dto.AuthorResponse;
import com.fluffy.comment.application.dto.CreateExamCommentResponse;
import com.fluffy.support.AbstractDocumentTest;
import jakarta.servlet.http.Cookie;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

class ExamCommentDocumentTest extends AbstractDocumentTest {

    @Test
    @DisplayName("댓글을 작성할 수 있다.")
    void createRootComment() throws Exception {
        CreateExamCommentWebRequest request = new CreateExamCommentWebRequest("댓글", null);
        CreateExamCommentResponse response = new CreateExamCommentResponse(
                1L,
                "댓글",
                1L,
                null,
                new AuthorResponse(1L, "사람", "https://image.com"),
                LocalDateTime.now()
        );
        when(examCommentService.createComment(any()))
                .thenReturn(response);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/exams/{examId}/comments", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("access_token", "{ACCESS_TOKEN}"))
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpectAll(
                        status().isOk(),
                        content().json(objectMapper.writeValueAsString(response))
                )
                .andDo(restDocs.document(
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
                ));
    }

    @Test
    @DisplayName("답글을 작성할 수 있다.")
    void createReplyComment() throws Exception {
        CreateExamCommentWebRequest request = new CreateExamCommentWebRequest("댓글", null);
        CreateExamCommentResponse response = new CreateExamCommentResponse(
                2L,
                "답글",
                1L,
                1L,
                new AuthorResponse(1L, "사람", "https://image.com"),
                LocalDateTime.now()
        );
        when(examCommentService.createComment(any()))
                .thenReturn(response);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/exams/{examId}/comments", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("access_token", "{ACCESS_TOKEN}"))
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpectAll(
                        status().isOk(),
                        content().json(objectMapper.writeValueAsString(response))
                )
                .andDo(restDocs.document(
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
                ));
    }

    @Test
    @DisplayName("댓글을 삭제할 수 있다.")
    void deleteComment() throws Exception {
        DeleteExamCommentWebResponse response = new DeleteExamCommentWebResponse(1L);
        when(examCommentService.deleteComment(any()))
                .thenReturn(1L);

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/v1/comments/{commentId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("access_token", "{ACCESS_TOKEN}"))
                )
                .andExpectAll(
                        status().isOk(),
                        content().json(objectMapper.writeValueAsString(response))
                )
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("commentId").description("댓글 식별자")
                        ),
                        responseFields(
                                fieldWithPath("id").description("삭제된 댓글 식별자")
                        )
                ));
    }
}
