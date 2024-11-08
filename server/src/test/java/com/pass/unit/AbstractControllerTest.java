package com.pass.unit;

import com.pass.form.ui.FormController;
import com.pass.form.command.application.FormService;
import com.pass.form.query.application.FormQueryService;
import io.restassured.mapper.ObjectMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest({
        FormController.class,
})
@ActiveProfiles("test")
public abstract class AbstractControllerTest {

    @MockBean
    protected MockMvc mockMvc;

    @MockBean
    protected ObjectMapper objectMapper;

    @MockBean
    protected FormService formService;

    @MockBean
    protected FormQueryService formQueryService;
}
