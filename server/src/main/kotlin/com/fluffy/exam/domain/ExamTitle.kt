package com.fluffy.exam.domain

import com.fluffy.global.exception.BadRequestException
import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class ExamTitle(

    @field:Column(name = "title", nullable = false)
    val value: String
) {

    companion object {

        private const val MAX_TITLE_LENGTH = 100
    }

    init {
        validateTitleNotBlank()
        validateTitleLength()
    }

    private fun validateTitleNotBlank() {
        if (value.isBlank()) {
            throw BadRequestException("시험 제목은 비어있을 수 없습니다.")
        }
    }

    private fun validateTitleLength() {
        if (value.length > MAX_TITLE_LENGTH) {
            throw BadRequestException("시험 제목은 ${MAX_TITLE_LENGTH}자 이하여야 합니다.")
        }
    }
}