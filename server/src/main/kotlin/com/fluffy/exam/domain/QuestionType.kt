package com.fluffy.exam.domain

import com.fluffy.global.exception.NotFoundException

enum class QuestionType {

    SHORT_ANSWER,
    LONG_ANSWER,
    SINGLE_CHOICE,
    MULTIPLE_CHOICE,
    TRUE_OR_FALSE,
    ;

    companion object {
        fun from(question: String): QuestionType {
            return entries.find { it.name.equals(question, ignoreCase = true) }
                ?: throw NotFoundException("존재하지 않는 문제 유형입니다. 문제 유형: $question")
        }
    }
}