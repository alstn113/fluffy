package com.pass.exam.ui;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class ExamWebConfig implements WebMvcConfigurer {

    private final ExamStatusConverter examStatusConverter;

    @Override
    public void addFormatters(org.springframework.format.FormatterRegistry registry) {
        registry.addConverter(examStatusConverter);
    }
}
