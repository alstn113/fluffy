package com.fluffy.submission.application

import com.fluffy.exam.domain.Question
import com.fluffy.exam.domain.QuestionOption
import com.fluffy.exam.domain.QuestionType.LONG_ANSWER
import com.fluffy.exam.domain.QuestionType.MULTIPLE_CHOICE
import com.fluffy.exam.domain.QuestionType.SHORT_ANSWER
import com.fluffy.exam.domain.QuestionType.SINGLE_CHOICE
import com.fluffy.exam.domain.QuestionType.TRUE_OR_FALSE
import com.fluffy.global.exception.BadRequestException
import com.fluffy.submission.application.request.QuestionResponseRequest
import com.fluffy.submission.application.response.AnswerBaseResponse
import com.fluffy.submission.application.response.ChoiceAnswerResponse
import com.fluffy.submission.application.response.ChoiceResponse
import com.fluffy.submission.application.response.TextAnswerResponse
import com.fluffy.submission.domain.Answer
import com.fluffy.submission.domain.Choice

object AnswerAssembler {

    fun toAnswer(question: Question, request: QuestionResponseRequest): Answer {
        if (request.answers.isEmpty()) {
            throw BadRequestException("문제에 대한 응답이 비어 있습니다.")
        }

        return when (question.type) {
            SHORT_ANSWER, LONG_ANSWER -> toTextAnswer(question, request)
            SINGLE_CHOICE, TRUE_OR_FALSE -> toSingleChoiceAnswer(question, request)
            MULTIPLE_CHOICE -> toMultipleChoiceAnswer(question, request)
        }
    }

    private fun toTextAnswer(question: Question, request: QuestionResponseRequest): Answer {
        if (request.answers.size != 1) {
            throw BadRequestException("텍스트 응답은 하나만 제출할 수 있습니다.")
        }

        return Answer.textAnswer(
            questionId = question.id,
            text = request.answers.first(),
        )
    }

    private fun toSingleChoiceAnswer(question: Question, request: QuestionResponseRequest): Answer {
        if (request.answers.size != 1) {
            throw BadRequestException("응답은 하나만 제출할 수 있습니다.")
        }

        val options = question.options
        val choice = request.answers.first()

        val questionOption = options.find { it.text == choice }
            ?: throw BadRequestException("존재하지 않는 선택지입니다.")

        return Answer.choiceAnswer(question.id, listOf(Choice(questionOption.id)))
    }

    private fun toMultipleChoiceAnswer(question: Question, request: QuestionResponseRequest): Answer {
        val options = question.options
        val choices = request.answers

        val choiceList = choices.map { choice ->
            val questionOption = options.find { it.text == choice }
                ?: throw BadRequestException("존재하지 않는 선택지입니다.")
            Choice(questionOption.id)
        }

        return Answer.choiceAnswer(question.id, choiceList)
    }


    fun toDetailAnswers(questions: List<Question>, answers: List<Answer>): List<AnswerBaseResponse> {
        return questions.indices.map { index ->
            val question = questions[index]
            val answer = answers[index]

            when (question.type) {
                SHORT_ANSWER, LONG_ANSWER -> toDetailTextAnswer(answer, question)
                SINGLE_CHOICE, TRUE_OR_FALSE, MULTIPLE_CHOICE -> toDetailChoiceAnswer(answer, question)
            }
        }
    }

    private fun toDetailTextAnswer(answer: Answer, question: Question): AnswerBaseResponse {
        return TextAnswerResponse(
            answer.id,
            question.id,
            question.text,
            question.type.name,
            answer.text,
            question.correctAnswer,
        )
    }

    private fun toDetailChoiceAnswer(answer: Answer, question: Question): AnswerBaseResponse {
        return ChoiceAnswerResponse(
            answer.id,
            question.id,
            question.text,
            question.type.name,
            toChoiceResponses(question.options, answer.choices),
        )
    }

    private fun toChoiceResponses(options: List<QuestionOption>, choices: List<Choice>): List<ChoiceResponse> {
        return options.map { option ->
            ChoiceResponse(
                option.id,
                option.text,
                option.isCorrect,
                choices.any { choice -> choice.questionOptionId == option.id },
            )
        }
    }
}
