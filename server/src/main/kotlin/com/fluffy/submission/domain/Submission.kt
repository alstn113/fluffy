package com.fluffy.submission.domain

import com.fluffy.infra.persistence.AuditableEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity
class Submission private constructor(
    examId: Long,
    memberId: Long,
    id: Long = 0,
) : AuditableEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = id
        protected set

    @field:Column(nullable = false)
    var examId: Long = examId
        protected set

    @field:Column(nullable = false)
    var memberId: Long = memberId
        protected set

    @OneToMany(mappedBy = "submission", cascade = [CascadeType.ALL], orphanRemoval = true)
    private val _answers: MutableList<Answer> = mutableListOf()
    val answers: List<Answer> get() = _answers.toList()

    companion object {
        fun create(examId: Long, memberId: Long, answers: List<Answer>): Submission {
            val submission = Submission(examId, memberId)
            submission.addAnswers(answers)

            return submission
        }
    }

    fun addAnswers(answers: List<Answer>) {
        answers.forEach { it.updateSubmission(this) }
        _answers.addAll(answers)
    }

    fun isNotWrittenBy(memberId: Long): Boolean {
        return this.memberId != memberId
    }
}