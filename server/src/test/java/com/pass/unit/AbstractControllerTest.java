package com.pass.unit;

import com.pass.exam.ui.ExamController;
import com.pass.exam.command.application.ExamService;
import com.pass.exam.query.application.ExamQueryService;
import io.restassured.mapper.ObjectMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest({
        ExamController.class,
})
@ActiveProfiles("test")
public abstract class AbstractControllerTest {

    @MockBean
    protected MockMvc mockMvc;

    @MockBean
    protected ObjectMapper objectMapper;

    @MockBean
    protected ExamService examService;

    @MockBean
    protected ExamQueryService examQueryService;
}
