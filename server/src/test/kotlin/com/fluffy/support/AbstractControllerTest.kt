package com.fluffy.support

import com.fasterxml.jackson.databind.ObjectMapper
import com.fluffy.auth.application.AuthService
import com.fluffy.auth.ui.AuthController
import com.fluffy.comment.application.ExamCommentQueryService
import com.fluffy.comment.application.ExamCommentService
import com.fluffy.comment.ui.ExamCommentController
import com.fluffy.exam.application.ExamImageService
import com.fluffy.exam.application.ExamLikeService
import com.fluffy.exam.application.ExamQueryService
import com.fluffy.exam.application.ExamService
import com.fluffy.exam.ui.ExamController
import com.fluffy.exam.ui.ExamLikeController
import com.fluffy.global.web.Accessor
import com.fluffy.global.web.AuthArgumentResolver
import com.fluffy.global.web.cookie.CookieManager
import com.fluffy.oauth2.application.OAuth2Service
import com.fluffy.oauth2.ui.OAuth2Controller
import com.fluffy.submission.application.SubmissionQueryService
import com.fluffy.submission.application.SubmissionService
import com.fluffy.submission.ui.SubmissionController
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc

@WebMvcTest(
    value = [
        AuthController::class,
        ExamController::class,
        ExamCommentController::class,
        ExamLikeController::class,
        OAuth2Controller::class,
        SubmissionController::class
    ]
)
@ActiveProfiles("test")
abstract class AbstractControllerTest {

    @Autowired
    protected lateinit var mockMvc: MockMvc

    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    @MockkBean
    protected lateinit var authArgumentResolver: AuthArgumentResolver

    @MockkBean
    protected lateinit var authService: AuthService

    @MockkBean
    protected lateinit var cookieManager: CookieManager

    @MockkBean
    protected lateinit var examService: ExamService

    @MockkBean
    protected lateinit var examLikeService: ExamLikeService

    @MockkBean
    protected lateinit var examQueryService: ExamQueryService

    @MockkBean
    protected lateinit var oauth2Service: OAuth2Service

    @MockkBean
    protected lateinit var submissionService: SubmissionService

    @MockkBean
    protected lateinit var submissionQueryService: SubmissionQueryService

    @MockkBean
    protected lateinit var examImageService: ExamImageService

    @MockkBean
    protected lateinit var examCommentService: ExamCommentService

    @MockkBean
    protected lateinit var examCommentQueryService: ExamCommentQueryService

    @BeforeEach
    fun setUp() {
        every { authArgumentResolver.supportsParameter(any()) } returns true
        every { authArgumentResolver.resolveArgument(any(), any(), any(), any()) } returns Accessor(1L)
    }
}
