package com.fluffy.support

import com.fluffy.support.AbstractDocumentTest.RestDocsConfig
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler
import org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.CharacterEncodingFilter

@Import(RestDocsConfig::class)
@ExtendWith(RestDocumentationExtension::class)
abstract class AbstractDocumentTest : AbstractControllerTest() {

    @Autowired
    lateinit var restDocs: RestDocumentationResultHandler

    @BeforeEach
    fun setUp(
        webApplicationContext: WebApplicationContext,
        restDocumentation: RestDocumentationContextProvider,
    ) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply<DefaultMockMvcBuilder>(
                documentationConfiguration(restDocumentation)
                    .uris()
                    .withScheme("https")
                    .withHost("api.fluffy.com")
                    .withPort(443)
                    .and()
                    .operationPreprocessors()
                    .withRequestDefaults(modifyUris().host("api.fluffy.com").removePort(), prettyPrint())
                    .withResponseDefaults(prettyPrint())
            )
            .alwaysDo<DefaultMockMvcBuilder>(print())
            .alwaysDo<DefaultMockMvcBuilder>(restDocs)
            .addFilter<DefaultMockMvcBuilder>(CharacterEncodingFilter("UTF-8", true))
            .build()
    }

    @TestConfiguration
    class RestDocsConfig {

        @Bean
        fun restDocs(): RestDocumentationResultHandler {
            return MockMvcRestDocumentation.document("{class-name}/{method-name}")
        }
    }
}
