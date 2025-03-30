package com.fluffy.exam.domain

import com.fluffy.global.exception.BadRequestException
import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class ExamDescription(

    @field:Column(name = "description", nullable = false)
    val value: String
) {

    companion object {

        private const val MAX_DESCRIPTION_LENGTH = 2000

        fun empty() = ExamDescription("")
    }

    init {
        validateDescriptionLength()
    }

    private fun validateDescriptionLength() {
        if (value.length > MAX_DESCRIPTION_LENGTH) {
            throw BadRequestException("시험 설명은 ${MAX_DESCRIPTION_LENGTH}자 이하여야 합니다.")
        }
    }
}