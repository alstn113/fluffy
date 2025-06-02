package com.fluffy.exam.application

import com.fluffy.exam.application.response.ExamDetailResponse
import com.fluffy.exam.application.response.ExamDetailSummaryResponse
import com.fluffy.exam.application.response.ExamWithAnswersResponse
import com.fluffy.exam.domain.ExamRepository
import com.fluffy.exam.domain.ExamStatus
import com.fluffy.exam.domain.dto.ExamDetailSummaryDto
import com.fluffy.exam.domain.dto.ExamSummaryDto
import com.fluffy.exam.domain.dto.MyExamSummaryDto
import com.fluffy.exam.domain.dto.SubmittedExamSummaryDto
import com.fluffy.exam.domain.findByIdOrThrow
import com.fluffy.global.exception.ForbiddenException
import com.fluffy.global.exception.NotFoundException
import com.fluffy.global.response.PageResponse
import com.fluffy.global.web.Accessor
import com.fluffy.reaction.domain.Like
import com.fluffy.reaction.domain.LikeQueryService
import com.fluffy.reaction.domain.LikeTarget
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ExamQueryService(
    private val examRepository: ExamRepository,
    private val likeQueryService: LikeQueryService,
) {

    @Transactional(readOnly = true)
    fun getPublishedExamSummaries(pageable: Pageable): PageResponse<ExamSummaryDto> {
        val summaries = examRepository.findPublishedExamSummaries(pageable)

        return PageResponse.of(summaries)
    }

    @Transactional(readOnly = true)
    fun getMyExamSummaries(pageable: Pageable, status: ExamStatus, accessor: Accessor): PageResponse<MyExamSummaryDto> {
        val summaries = examRepository.findMyExamSummaries(pageable, status, accessor.id)

        return PageResponse.of(summaries)
    }

    @Transactional(readOnly = true)
    fun getExamDetailSummary(examId: Long, accessor: Accessor): ExamDetailSummaryResponse {
        val dto: ExamDetailSummaryDto = examRepository.findExamDetailSummary(examId)
            ?: throw NotFoundException("존재하지 않는 시험입니다. 시험 식별자: $examId")
        val isLiked = likeQueryService.isLiked(Like(LikeTarget.EXAM, examId), accessor)

        return ExamAssembler.toDetailSummaryResponse(dto, isLiked)
    }

    @Transactional(readOnly = true)
    @Cacheable(value = ["examDetail"], key = "#examId")
    fun getExamDetail(examId: Long): ExamDetailResponse {
        val exam = examRepository.findByIdOrThrow(examId)

        return ExamAssembler.toDetailResponse(exam)
    }

    @Transactional(readOnly = true)
    fun getExamWithAnswers(examId: Long, accessor: Accessor): ExamWithAnswersResponse {
        val exam = examRepository.findByIdOrThrow(examId)
        if (exam.isNotWrittenBy(accessor.id)) {
            throw ForbiddenException("해당 시험에 접근할 수 없습니다.")
        }

        return ExamAssembler.toWithAnswersResponse(exam)
    }

    @Transactional(readOnly = true)
    fun getSubmittedExamSummaries(pageable: Pageable, accessor: Accessor): PageResponse<SubmittedExamSummaryDto> {
        val summaries = examRepository.findSubmittedExamSummaries(pageable, accessor.id)

        return PageResponse.of(summaries)
    }
}
