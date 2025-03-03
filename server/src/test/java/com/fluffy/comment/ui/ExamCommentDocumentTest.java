package com.fluffy.comment.ui;


import com.fluffy.comment.application.dto.AuthorResponse;
import com.fluffy.comment.application.dto.CreateExamCommentResponse;
import com.fluffy.comment.domain.dto.AuthorDto;
import com.fluffy.comment.domain.dto.ExamReplyCommentDto;
import com.fluffy.comment.domain.dto.ExamRootCommentDto;
import com.fluffy.comment.ui.dto.CreateExamCommentWebRequest;
import com.fluffy.comment.ui.dto.DeleteExamCommentWebResponse;
import com.fluffy.support.AbstractDocumentTest;
import jakarta.servlet.http.Cookie;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ExamCommentDocumentTest extends AbstractDocumentTest {

    @Test
    @DisplayName("해당 시험에 대한 루트 댓글들을 조회할 수 있다.")
    void getRootComments() throws Exception {
        when(examCommentQueryService.getRootComments(any()))
                .thenReturn(List.of(
                        new ExamRootCommentDto(1L, "댓글",
                                new AuthorDto(1L, "사람", "https://image.com"),
                                0L,
                                false,
                                LocalDateTime.now(),
                                LocalDateTime.now()
                        ),
                        new ExamRootCommentDto(2L, "",
                                new AuthorDto(-1L, "", ""),
                                1L,
                                true,
                                LocalDateTime.now(),
                                LocalDateTime.now()
                        )
                ));

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/exams/{examId}/comments", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpectAll(
                        status().isOk()
                )
                .andDo(restDocs.document(
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
                ));
    }

    @Test
    @DisplayName("시험에서 댓글에 대한 답글들을 조회할 수 있다.")
    void getReplyComments() throws Exception {
        when(examCommentQueryService.getReplyComments(any()))
                .thenReturn(List.of(
                        new ExamReplyCommentDto(1L, "답글",
                                new AuthorDto(1L, "사람", "https://image.com"),
                                LocalDateTime.now(),
                                LocalDateTime.now()
                        )
                ));

        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/exams/comments/{commentId}/replies", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpectAll(
                        status().isOk()
                )
                .andDo(restDocs.document(
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
                ));
    }

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

        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/v1/exams/comments/{commentId}", 1L)
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
