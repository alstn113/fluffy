package com.fluffy.exam.api;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fluffy.support.AbstractDocumentTest;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ExamLikeDocumentTest extends AbstractDocumentTest {

    @Test
    @DisplayName("시험에 좋아요를 할 수 있다.")
    void like() throws Exception {
        mockMvc.perform(post("/api/v1/exams/{examId}/like", 1)
                        .cookie(new Cookie("accessToken", "{ACCESS_TOKEN}")))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("examId").description("시험 ID")
                        )
                ));
    }

    @Test
    @DisplayName("시험에 좋아요를 취소할 수 있다.")
    void unlike() throws Exception {
        mockMvc.perform(post("/api/v1/exams/{examId}/like", 1)
                        .cookie(new Cookie("accessToken", "{ACCESS_TOKEN}")))
                .andExpect(status().isOk())
                .andDo(restDocs.document(
                        pathParameters(
                                parameterWithName("examId").description("시험 ID")
                        )
                ));
    }
}
