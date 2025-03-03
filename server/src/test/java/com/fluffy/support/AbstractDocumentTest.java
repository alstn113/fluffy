package com.fluffy.support;

import com.fluffy.support.AbstractDocumentTest.RestDocsConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;


@Import(RestDocsConfig.class)
@ExtendWith(RestDocumentationExtension.class)
public abstract class AbstractDocumentTest extends AbstractControllerTest {

    @Autowired
    protected RestDocumentationResultHandler restDocs;

    @BeforeEach
    public void setUp(
            WebApplicationContext webApplicationContext,
            RestDocumentationContextProvider restDocumentation
    ) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation)
                        .operationPreprocessors()
                        .withRequestDefaults(
                                prettyPrint(),
                                modifyUris().scheme("https").host("api.fluffy.com").removePort()
                        )
                        .withResponseDefaults(prettyPrint())
                )
                .alwaysDo(print())
                .alwaysDo(restDocs)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    @TestConfiguration
    static class RestDocsConfig {

        @Bean
        public RestDocumentationResultHandler restDocs() {
            return MockMvcRestDocumentation.document("{class-name}/{method-name}");
        }
    }
}
