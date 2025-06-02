package com.fluffy.exam.domain

import com.fluffy.global.exception.BadRequestException
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class QuestionOption(
    text: String,
    isCorrect: Boolean,
    id: Long = 0,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = id
        protected set

    @field:Column(nullable = false)
    var text: String = text
        protected set

    @field:Column(nullable = false)
    var isCorrect: Boolean = isCorrect
        protected set

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    var question: Question? = null

    companion object {

        private const val MAX_TEXT_LENGTH = 200
    }

    init {
        validateTextNotBlank()
        validateTextLength()
    }

    fun updateQuestion(question: Question) {
        this.question = question
    }

    private fun validateTextNotBlank() {
        if (text.isBlank()) {
            throw BadRequestException("문제 선택지의 내용은 비어있을 수 없습니다.")
        }
    }

    private fun validateTextLength() {
        if (text.length > MAX_TEXT_LENGTH) {
            throw BadRequestException("문제 선택지의 내용은 ${MAX_TEXT_LENGTH}자 이하여야 합니다.")
        }
    }
}