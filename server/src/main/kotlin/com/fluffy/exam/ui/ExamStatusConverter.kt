package com.fluffy.exam.ui

import com.fluffy.exam.domain.ExamStatus
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

@Component
class ExamStatusConverter : Converter<String, ExamStatus> {

    override fun convert(source: String): ExamStatus {
        return ExamStatus.from(source)
    }
}
