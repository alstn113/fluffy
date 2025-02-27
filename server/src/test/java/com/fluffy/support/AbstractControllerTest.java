package com.fluffy.support;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fluffy.auth.api.AuthController;
import com.fluffy.auth.application.AuthService;
import com.fluffy.comment.api.ExamCommentController;
import com.fluffy.comment.application.ExamCommentQueryService;
import com.fluffy.comment.application.ExamCommentService;
import com.fluffy.exam.api.ExamController;
import com.fluffy.exam.api.ExamLikeController;
import com.fluffy.exam.application.ExamImageService;
import com.fluffy.exam.application.ExamLikeService;
import com.fluffy.exam.application.ExamQueryService;
import com.fluffy.exam.application.ExamService;
import com.fluffy.global.web.Accessor;
import com.fluffy.global.web.AuthArgumentResolver;
import com.fluffy.global.web.cookie.CookieManager;
import com.fluffy.oauth2.api.OAuth2Controller;
import com.fluffy.oauth2.application.OAuth2Service;
import com.fluffy.submission.api.SubmissionController;
import com.fluffy.submission.application.SubmissionQueryService;
import com.fluffy.submission.application.SubmissionService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest({
        AuthController.class,
        ExamController.class,
        ExamCommentController.class,
        ExamLikeController.class,
        OAuth2Controller.class,
        SubmissionController.class,
})
@ActiveProfiles("test")
public abstract class AbstractControllerTest {

    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected AuthArgumentResolver authArgumentResolver;

    @MockBean
    protected AuthService authService;

    @MockBean
    protected CookieManager cookieManager;

    @MockBean
    protected ExamService examService;

    @MockBean
    protected ExamLikeService examLikeService;

    @MockBean
    protected ExamQueryService examQueryService;

    @MockBean
    protected OAuth2Service oauth2Service;

    @MockBean
    protected SubmissionService submissionService;

    @MockBean
    protected SubmissionQueryService submissionQueryService;

    @MockBean
    protected ExamImageService examImageService;

    @MockBean
    protected ExamCommentService examCommentService;

    @MockBean
    protected ExamCommentQueryService examCommentQueryService;

    @BeforeEach
    public void setUp() {
        when(authArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(new Accessor(1L));
    }
}
