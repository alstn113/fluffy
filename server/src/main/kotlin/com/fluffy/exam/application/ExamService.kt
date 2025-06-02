package com.fluffy.exam.application

import com.fluffy.exam.application.request.CreateExamRequest
import com.fluffy.exam.application.request.PublishExamRequest
import com.fluffy.exam.application.request.UpdateExamDescriptionRequest
import com.fluffy.exam.application.request.UpdateExamQuestionsRequest
import com.fluffy.exam.application.request.UpdateExamTitleRequest
import com.fluffy.exam.application.response.CreateExamResponse
import com.fluffy.exam.domain.Exam
import com.fluffy.exam.domain.ExamRepository
import com.fluffy.exam.domain.Question
import com.fluffy.exam.domain.findByIdOrThrow
import com.fluffy.global.exception.ForbiddenException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class ExamService(
    private val examRepository: ExamRepository,
) {

    @Transactional
    fun create(request: CreateExamRequest): CreateExamResponse {
        val exam = Exam.create(request.title, request.accessor.id)
        val savedExam = examRepository.save(exam)

        return ExamAssembler.toCreateResponse(savedExam)
    }

    @Transactional
    fun updateQuestions(request: UpdateExamQuestionsRequest) {
        val exam = validateExamAuthor(request.examId, request.accessor.id)

        val questions: List<Question> = QuestionAssembler.toQuestions(request.questions)
        exam.updateQuestions(questions)
    }

    @Transactional
    fun publish(request: PublishExamRequest) {
        val exam = validateExamAuthor(request.examId, request.accessor.id)

        val questions: List<Question> = QuestionAssembler.toQuestions(request.questions)
        exam.updateQuestions(questions)

        exam.publish()
    }

    @Transactional
    fun updateTitle(request: UpdateExamTitleRequest) {
        val exam = validateExamAuthor(request.examId, request.accessor.id)

        exam.updateTitle(request.title)
    }

    @Transactional
    fun updateDescription(request: UpdateExamDescriptionRequest) {
        val exam = validateExamAuthor(request.examId, request.accessor.id)

        exam.updateDescription(request.description)
    }

    private fun validateExamAuthor(examId: Long, memberId: Long): Exam {
        val exam = examRepository.findByIdOrThrow(examId)

        if (exam.isNotWrittenBy(memberId)) {
            throw ForbiddenException("해당 사용자가 작성한 시험이 아닙니다. 사용자 식별자: $memberId, 시험 식별자: $examId")
        }

        return exam
    }
}
