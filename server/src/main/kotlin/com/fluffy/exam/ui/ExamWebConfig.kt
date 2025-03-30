package com.fluffy.exam.ui

import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class ExamWebConfig(
    private val examStatusConverter: ExamStatusConverter
) : WebMvcConfigurer {

    override fun addFormatters(registry: FormatterRegistry) {
        registry.addConverter(examStatusConverter)
    }
}