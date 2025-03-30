package com.fluffy.exam.domain

import com.fluffy.global.exception.NotFoundException

enum class ExamStatus {

    DRAFT,
    PUBLISHED,
    ;

    companion object {

        fun from(status: String): ExamStatus {
            return entries.find { it.name.equals(status, ignoreCase = true) }
                ?: throw NotFoundException("존재하지 않는 시험 상태입니다. 시험 상태: $status")
        }
    }

    fun isPublished(): Boolean {
        return this == PUBLISHED
    }
}