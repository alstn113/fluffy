package com.fluffy.exam.domain

import com.fluffy.global.exception.BadRequestException
import com.fluffy.infra.persistence.AuditableEntity
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity
class Exam(
    title: ExamTitle,
    description: ExamDescription,
    status: ExamStatus,
    memberId: Long,
    isSingleAttempt: Boolean,
    id: Long = 0,
) : AuditableEntity() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = id
        protected set

    @Embedded
    private var _title: ExamTitle = title
    val title: String get() = _title.value

    @field:Column(nullable = false)
    private var _description: ExamDescription = description
    val description: String get() = _description.value

    @field:Column(nullable = false, columnDefinition = "VARCHAR(20)")
    @Enumerated(EnumType.STRING)
    var status: ExamStatus = status
        protected set

    @field:Column(nullable = false)
    var memberId: Long = memberId
        protected set

    @field:Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    var isSingleAttempt: Boolean = isSingleAttempt
        protected set

    @OneToMany(mappedBy = "exam", cascade = [CascadeType.ALL], orphanRemoval = true)
    private val _questions: MutableList<Question> = mutableListOf()
    val questions: List<Question> get() = _questions.toList()

    companion object {

        private const val MAX_QUESTION_SIZE = 200

        fun create(title: String, memberId: Long): Exam {
            return Exam(
                title = ExamTitle(title),
                description = ExamDescription.empty(),
                status = ExamStatus.DRAFT,
                memberId = memberId,
                isSingleAttempt = false,
            )
        }
    }

    fun updateQuestions(questions: List<Question>) {
        if (status.isPublished()) {
            throw BadRequestException("시험이 출시된 후에는 문제를 변경할 수 없습니다.")
        }

        if (questions.size > MAX_QUESTION_SIZE) {
            throw BadRequestException("시험 문제는 ${MAX_QUESTION_SIZE}개 이하여야 합니다.")
        }

        this._questions.clear()
        questions.forEach { it.updateExam(this) }
        this._questions.addAll(questions)

        updateTimestamp()
    }

    fun publish() {
        if (status.isPublished()) {
            throw BadRequestException("시험은 이미 출시되었습니다.")
        }

        if (questions.isEmpty()) {
            throw BadRequestException("시험을 출시하기 위해서는 최소 1개 이상의 문제를 추가해야 합니다.")
        }

        this.status = ExamStatus.PUBLISHED
    }

    fun updateTitle(title: String) {
        if (status.isPublished()) {
            throw BadRequestException("시험이 출시된 후에는 제목을 수정할 수 없습니다.")
        }

        this._title = ExamTitle(title)
    }

    fun updateDescription(description: String) {
        if (status.isPublished()) {
            throw BadRequestException("시험이 출시된 후에는 설명을 수정할 수 없습니다.")
        }

        this._description = ExamDescription(description)
    }

    fun updateIsSingleAttempt(isSingleAttempt: Boolean) {
        if (status.isPublished()) {
            throw BadRequestException("시험이 출시된 후에는 응시 횟수 제한을 수정할 수 없습니다.")
        }

        this.isSingleAttempt = isSingleAttempt
    }

    fun isNotWrittenBy(memberId: Long): Boolean {
        return this.memberId != memberId
    }

    fun isNotPublished(): Boolean {
        return !status.isPublished()
    }
}
