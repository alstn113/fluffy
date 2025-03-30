package com.fluffy.exam.application

import com.fluffy.exam.application.request.question.*
import com.fluffy.exam.application.response.*
import com.fluffy.exam.domain.Question
import com.fluffy.exam.domain.QuestionOption
import com.fluffy.exam.domain.QuestionType
import com.fluffy.exam.domain.QuestionType.*

object QuestionAssembler {

    fun toQuestions(requests: List<QuestionRequest>): List<Question> {
        return requests.stream()
            .map { request: QuestionRequest -> toQuestion(request) }
            .toList()
    }

    private fun toQuestion(request: QuestionRequest): Question {
        val questionType = QuestionType.from(request.type)

        return when (questionType) {
            SHORT_ANSWER -> toQuestion(request as ShortAnswerQuestionRequest)
            LONG_ANSWER -> toQuestion(request as LongAnswerQuestionRequest)
            SINGLE_CHOICE -> toQuestion(request as SingleChoiceQuestionRequest)
            MULTIPLE_CHOICE -> toQuestion(request as MultipleChoiceQuestionRequest)
            TRUE_OR_FALSE -> toQuestion(request as TrueOrFalseQuestionRequest)
        }
    }

    private fun toQuestion(request: ShortAnswerQuestionRequest): Question {
        return Question.shortAnswer(
            request.text,
            request.passage,
            request.correctAnswer
        )
    }

    private fun toQuestion(request: LongAnswerQuestionRequest): Question {
        return Question.longAnswer(request.text, request.passage)
    }

    private fun toQuestion(request: SingleChoiceQuestionRequest): Question {
        val options = toQuestionOptions(request.options)

        return Question.singleChoice(request.text, request.passage, options)
    }

    private fun toQuestion(request: MultipleChoiceQuestionRequest): Question {
        val options = toQuestionOptions(request.options)

        return Question.multipleChoice(request.text, request.passage, options)
    }

    private fun toQuestion(request: TrueOrFalseQuestionRequest): Question {
        return Question.trueOrFalse(request.text, request.passage, request.trueOrFalse)
    }

    private fun toQuestionOptions(requests: List<QuestionOptionRequest>): List<QuestionOption> {
        return requests.stream()
            .map { request: QuestionOptionRequest -> toQuestionOption(request) }
            .toList()
    }

    private fun toQuestionOption(request: QuestionOptionRequest): QuestionOption {
        return QuestionOption(request.text, request.isCorrect)
    }

    fun toResponses(questions: List<Question>): List<QuestionResponse> {
        return questions.map { toResponse(it) }
    }

    private fun toResponse(question: Question): QuestionResponse {
        return when (question.type) {
            SHORT_ANSWER, LONG_ANSWER -> toAnswerResponse(question)
            SINGLE_CHOICE, MULTIPLE_CHOICE, TRUE_OR_FALSE -> toChoiceResponse(question)
        }
    }

    private fun toAnswerResponse(question: Question): QuestionResponse {
        return AnswerQuestionResponse(
            question.id,
            question.text,
            question.passage,
            question.type.name
        )
    }

    private fun toChoiceResponse(question: Question): QuestionResponse {
        return ChoiceQuestionResponse(
            question.id,
            question.text,
            question.passage,
            question.type.name,
            QuestionOptionAssembler.toResponses(question.options)
        )
    }

    fun toWithAnswersResponses(questions: List<Question>): List<QuestionWithAnswersResponse> {
        return questions.map { toWithAnswersResponse(it) }
    }

    private fun toWithAnswersResponse(question: Question): QuestionWithAnswersResponse {
        return when (question.type) {
            SHORT_ANSWER, LONG_ANSWER -> toAnswerWithAnswersResponse(question)
            SINGLE_CHOICE, MULTIPLE_CHOICE, TRUE_OR_FALSE -> toChoiceWithAnswersResponse(question)
        }
    }

    private fun toAnswerWithAnswersResponse(question: Question): QuestionWithAnswersResponse {
        return AnswerQuestionWithAnswersResponse(
            question.id,
            question.text,
            question.passage,
            question.type.name,
            question.correctAnswer
        )
    }

    private fun toChoiceWithAnswersResponse(question: Question): QuestionWithAnswersResponse {
        return ChoiceQuestionWithAnswersResponse(
            question.id,
            question.text,
            question.passage,
            question.type.name,
            QuestionOptionAssembler.toWithAnswersResponses(question.options)
        )
    }
}