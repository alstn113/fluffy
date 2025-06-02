package com.fluffy.submission.domain

import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class Answer(
    text: String,
    questionId: Long,
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
    var questionId: Long = questionId
        protected set

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "submission_id", nullable = false)
    var submission: Submission? = null
        protected set

    @ElementCollection
    @CollectionTable(name = "answer_choice", joinColumns = [JoinColumn(name = "answer_id")])
    private val _choices: MutableSet<Choice> = mutableSetOf()
    val choices: List<Choice> get() = _choices.toList()

    companion object {

        fun textAnswer(text: String, questionId: Long): Answer {
            return Answer(
                text = text,
                questionId = questionId,
            )
        }

        fun choiceAnswer(questionId: Long, choices: List<Choice>): Answer {
            val answer = Answer(
                text = "",
                questionId = questionId,
            )
            answer.addChoices(choices)

            return answer
        }
    }

    fun addChoices(choices: List<Choice>) {
        _choices.addAll(choices)
    }

    fun updateSubmission(submission: Submission) {
        this.submission = submission
    }
}