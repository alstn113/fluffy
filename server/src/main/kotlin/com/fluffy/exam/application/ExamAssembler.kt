package com.fluffy.exam.application

import com.fluffy.exam.application.response.CreateExamResponse
import com.fluffy.exam.application.response.ExamDetailResponse
import com.fluffy.exam.application.response.ExamDetailSummaryResponse
import com.fluffy.exam.application.response.ExamWithAnswersResponse
import com.fluffy.exam.domain.Exam
import com.fluffy.exam.domain.dto.ExamDetailSummaryDto

object ExamAssembler {

    fun toCreateResponse(exam: Exam): CreateExamResponse {
        return CreateExamResponse(
            id = exam.id,
            title = exam.title,
        )
    }

    fun toDetailResponse(exam: Exam): ExamDetailResponse {
        val questions = QuestionAssembler.toResponses(exam.questions)

        return ExamDetailResponse(
            id = exam.id,
            title = exam.title,
            description = exam.description,
            status = exam.status.name,
            questions = questions,
            createdAt = exam.createdAt,
            updatedAt = exam.updatedAt,
        )
    }

    fun toWithAnswersResponse(exam: Exam): ExamWithAnswersResponse {
        val questions = QuestionAssembler.toWithAnswersResponses(exam.questions)

        return ExamWithAnswersResponse(
            id = exam.id,
            title = exam.title,
            description = exam.description,
            status = exam.status.name,
            questions = questions,
            createdAt = exam.createdAt,
            updatedAt = exam.updatedAt,
        )
    }

    fun toDetailSummaryResponse(dto: ExamDetailSummaryDto, isLiked: Boolean): ExamDetailSummaryResponse {
        return ExamDetailSummaryResponse(
            id = dto.id,
            title = dto.title,
            description = dto.description,
            status = dto.status.name,
            author = dto.author,
            questionCount = dto.questionCount,
            likeCount = dto.likeCount,
            isLiked = isLiked,
            createdAt = dto.createdAt,
            updatedAt = dto.updatedAt,
        )
    }
}