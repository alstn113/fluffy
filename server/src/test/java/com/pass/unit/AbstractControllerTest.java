package com.pass.unit;

import com.pass.auth.application.AuthService;
import com.pass.auth.ui.AuthController;
import com.pass.exam.application.ExamQueryService;
import com.pass.exam.application.ExamService;
import com.pass.exam.ui.ExamController;
import com.pass.global.web.cookie.CookieManager;
import com.pass.oauth2.application.OAuth2Service;
import com.pass.oauth2.ui.OAuth2Controller;
import com.pass.submission.application.SubmissionQueryService;
import com.pass.submission.application.SubmissionService;
import com.pass.submission.ui.SubmissionController;
import io.restassured.mapper.ObjectMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest({
        AuthController.class,
        ExamController.class,
        OAuth2Controller.class,
        SubmissionController.class
})
@ActiveProfiles("test")
public abstract class AbstractControllerTest {

    @MockBean
    protected MockMvc mockMvc;

    @MockBean
    protected ObjectMapper objectMapper;

    @MockBean
    protected AuthService authService;

    @MockBean
    protected CookieManager cookieManager;

    @MockBean
    protected ExamQueryService examQueryService;

    @MockBean
    protected ExamService examService;

    @MockBean
    protected OAuth2Service oauth2Service;

    @MockBean
    protected SubmissionQueryService submissionQueryService;

    @MockBean
    protected SubmissionService submissionService;
}
