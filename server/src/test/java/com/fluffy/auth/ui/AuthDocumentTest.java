package com.fluffy.auth.ui;

import com.fluffy.auth.application.response.MyInfoResponse;
import com.fluffy.global.web.cookie.CookieManager;
import com.fluffy.support.AbstractDocumentTest;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(CookieManager.class)
class AuthDocumentTest extends AbstractDocumentTest {

    @Test
    @DisplayName("나의 정보를 조회할 수 있다.")
    void getMyInfo() throws Exception {
        MyInfoResponse response = new MyInfoResponse(1L, "mock@gmail.com", "mock-name", "https://mock.com");

        when(authService.getMyInfo(any()))
                .thenReturn(response);

        mockMvc.perform(get("/api/v1/auth/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("access_token", "{ACCESS_TOKEN}"))
                )
                .andExpectAll(
                        status().isOk(),
                        content().json(objectMapper.writeValueAsString(response))
                )
                .andDo(restDocs.document(
                        responseFields(
                                fieldWithPath("id").description("사용자 식별자"),
                                fieldWithPath("email").description("사용자 이메일"),
                                fieldWithPath("name").description("사용자 이름"),
                                fieldWithPath("avatarUrl").description("사용자 프로필 이미지 URL")
                        )
                ));
    }

    @Test
    @DisplayName("로그아웃을 할 수 있다.")
    void logout() throws Exception {
        when(cookieManager.createExpiredAccessTokenCookie())
                .thenReturn(ResponseCookie.from("access_token", "")
                        .httpOnly(true)
                        .secure(true)
                        .domain(".fluffy.run")
                        .path("/")
                        .maxAge(0)
                        .build()
                );

        mockMvc.perform(post("/api/v1/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .cookie(new Cookie("access_token", "{ACCESS_TOKEN}"))
                )
                .andExpectAll(
                        status().isOk(),
                        header().doesNotExist("access_token")
                )
                .andDo(restDocs.document(
                ));
    }
}
