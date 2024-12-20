package com.fluffy.exam.api;

import com.fluffy.exam.domain.ExamStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ExamStatusConverter implements Converter<String, ExamStatus> {

    @Override
    public ExamStatus convert(@NonNull String source) {
        return ExamStatus.from(source);
    }
}
