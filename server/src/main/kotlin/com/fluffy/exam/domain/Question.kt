package com.fluffy.exam.domain

import com.fluffy.global.exception.BadRequestException
import jakarta.persistence.*

@Entity
class Question(
    text: String,
    passage: String,
    type: QuestionType,
    correctAnswer: String?,
    id: Long = 0,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = id
        protected set

    @field:Column(nullable = false, columnDefinition = "VARCHAR(200)")
    var text: String = text
        protected set

    @field:Column(nullable = false, columnDefinition = "TEXT")
    var passage: String = passage
        protected set

    @field:Column(nullable = false, columnDefinition = "VARCHAR(50)")
    @Enumerated(EnumType.STRING)
    var type: QuestionType = type
        protected set

    @field:Column
    var correctAnswer: String? = correctAnswer
        protected set

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "exam_id", nullable = false)
    var exam: Exam? = null

    @OneToMany(mappedBy = "question", cascade = [CascadeType.ALL], orphanRemoval = true)
    private val _options: MutableList<QuestionOption> = mutableListOf()
    val options: List<QuestionOption> get() = _options.toList()

    companion object {

        private const val MAX_TEXT_LENGTH = 2000
        private const val MAX_ANSWER_LENGTH = 200
        private const val MIN_OPTION_SIZE = 2
        private const val MAX_OPTION_SIZE = 10

        fun shortAnswer(
            text: String,
            passage: String,
            correctAnswer: String,
        ): Question {
            if (correctAnswer.isBlank() || correctAnswer.length > MAX_ANSWER_LENGTH) {
                throw BadRequestException("단답형 문제의 정답은 1~${MAX_ANSWER_LENGTH}자여야 합니다.")
            }

            return answer(
                text = text,
                passage = passage,
                type = QuestionType.SHORT_ANSWER,
                correctAnswer = correctAnswer,
            )
        }

        fun longAnswer(
            text: String,
            passage: String,
        ): Question {
            return answer(
                text = text,
                passage = passage,
                type = QuestionType.LONG_ANSWER,
                correctAnswer = null,
            )
        }

        fun singleChoice(
            text: String,
            passage: String,
            options: List<QuestionOption>,
        ): Question {
            if (options.count { it.isCorrect } != 1) {
                throw BadRequestException("객관식 단일 선택의 정답은 1개여야 합니다.")
            }

            return choice(
                text = text,
                passage = passage,
                type = QuestionType.SINGLE_CHOICE,
                options = options,
            )
        }

        fun multipleChoice(
            text: String,
            passage: String,
            options: List<QuestionOption>,
        ): Question {
            return choice(
                text = text,
                passage = passage,
                type = QuestionType.MULTIPLE_CHOICE,
                options = options,
            )
        }

        fun trueOrFalse(
            text: String,
            passage: String,
            trueOrFalse: Boolean,
        ): Question {
            return choice(
                text = text,
                passage = passage,
                type = QuestionType.TRUE_OR_FALSE,
                options = listOf(
                    QuestionOption(text = "TRUE", isCorrect = trueOrFalse),
                    QuestionOption(text = "FALSE", isCorrect = !trueOrFalse)
                ),
            )
        }

        private fun answer(
            text: String,
            passage: String,
            type: QuestionType,
            correctAnswer: String?,
        ): Question {
            return Question(
                text = text,
                passage = passage,
                type = type,
                correctAnswer = correctAnswer,
            )
        }

        private fun choice(
            text: String,
            passage: String,
            type: QuestionType,
            options: List<QuestionOption>,
        ): Question {
            val choice = Question(
                text = text,
                passage = passage,
                type = type,
                correctAnswer = null,
            )
            choice.addOptions(options)

            return choice
        }
    }

    init {
        validateTextLength()
    }

    fun updateExam(exam: Exam) {
        this.exam = exam
    }

    private fun validateTextLength() {
        if (text.isBlank() || text.length > MAX_TEXT_LENGTH) {
            throw BadRequestException("문제의 질문은 1~${MAX_TEXT_LENGTH}자여야 합니다.")
        }
    }

    private fun addOptions(options: List<QuestionOption>) {
        validateOptionSize(options)
        validateDuplicateOption(options)

        options.forEach { it.updateQuestion(this) }
        _options.addAll(options)
    }

    private fun validateOptionSize(options: List<QuestionOption>) {
        if (options.size !in MIN_OPTION_SIZE..MAX_OPTION_SIZE) {
            throw BadRequestException("문제 선택지는 ${MIN_OPTION_SIZE}~${MAX_OPTION_SIZE}개여야 합니다.")
        }
    }

    private fun validateDuplicateOption(options: List<QuestionOption>) {
        val optionTexts = options.map { it.text }
        if (optionTexts.size != optionTexts.toSet().size) {
            throw BadRequestException("중복된 문제 선택지의 내용은 허용되지 않습니다.")
        }
    }
}