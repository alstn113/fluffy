package com.fluffy.exam.ui

import com.fluffy.exam.application.ExamImageService
import com.fluffy.exam.application.ExamQueryService
import com.fluffy.exam.application.ExamService
import com.fluffy.exam.application.request.ExamImagePresignedUrlRequest
import com.fluffy.exam.application.response.*
import com.fluffy.exam.domain.ExamStatus
import com.fluffy.exam.domain.dto.ExamSummaryDto
import com.fluffy.exam.domain.dto.MyExamSummaryDto
import com.fluffy.exam.domain.dto.SubmittedExamSummaryDto
import com.fluffy.exam.ui.request.*
import com.fluffy.global.response.PageResponse
import com.fluffy.global.web.Accessor
import com.fluffy.global.web.Auth
import jakarta.validation.Valid
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
class ExamController(
    private val examService: ExamService,
    private val examImageService: ExamImageService,
    private val examQueryService: ExamQueryService,
) {

    @GetMapping("/api/v1/exams")
    fun getPublishedExamSummaries(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
    ): ResponseEntity<PageResponse<ExamSummaryDto>> {
        val pageable = PageRequest.of(page, size)
        val response = examQueryService.getPublishedExamSummaries(pageable)

        return ResponseEntity.ok(response)
    }

    @GetMapping("/api/v1/exams/mine")
    fun getMyExamSummaries(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(value = "status", defaultValue = "draft") status: ExamStatus,
        @Auth accessor: Accessor,
    ): ResponseEntity<PageResponse<MyExamSummaryDto>> {
        val pageable = PageRequest.of(page, size)
        val response = examQueryService.getMyExamSummaries(pageable, status, accessor)

        return ResponseEntity.ok(response)
    }

    @GetMapping("/api/v1/exams/{examId}/summary")
    fun getExamDetailSummary(
        @PathVariable examId: Long,
        @Auth(required = false) accessor: Accessor,
    ): ResponseEntity<ExamDetailSummaryResponse> {
        val response = examQueryService.getExamDetailSummary(examId, accessor)

        return ResponseEntity.ok(response)
    }

    @GetMapping("/api/v1/exams/{examId}")
    fun getExamDetail(@PathVariable examId: Long): ResponseEntity<ExamDetailResponse> {
        val response = examQueryService.getExamDetail(examId)

        return ResponseEntity.ok(response)
    }

    @GetMapping("/api/v1/exams/{examId}/with-answers")
    fun getExamWithAnswers(
        @PathVariable examId: Long,
        @Auth accessor: Accessor,
    ): ResponseEntity<ExamWithAnswersResponse> {
        val response = examQueryService.getExamWithAnswers(examId, accessor)

        return ResponseEntity.ok(response)
    }

    @GetMapping("/api/v1/exams/submitted")
    fun getSubmittedExamSummaries(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @Auth accessor: Accessor,
    ): ResponseEntity<PageResponse<SubmittedExamSummaryDto>> {
        val pageable = PageRequest.of(page, size)
        val response = examQueryService.getSubmittedExamSummaries(pageable, accessor)

        return ResponseEntity.ok(response)
    }

    @PostMapping("/api/v1/exams")
    fun create(
        @RequestBody @Valid request: CreateExamWebRequest,
        @Auth accessor: Accessor,
    ): ResponseEntity<CreateExamResponse> {
        val response = examService.create(request.toAppRequest(accessor))
        val location = URI.create("/api/v1/exams/${response.id}")

        return ResponseEntity.created(location).body(response)
    }

    @PostMapping("/api/v1/exams/{examId}/publish")
    fun publish(
        @PathVariable examId: Long,
        @RequestBody @Valid request: PublishExamWebRequest,
        @Auth accessor: Accessor,
    ): ResponseEntity<Unit> {
        val appRequest = request.toAppRequest(examId, accessor)
        examService.publish(appRequest)

        return ResponseEntity.ok().build()
    }

    @PostMapping("/api/v1/exams/{examId}/images/presigned-url")
    fun createPresignedUrl(
        @PathVariable examId: Long,
        @RequestBody request: ExamImagePresignedUrlRequest,
        @Auth accessor: Accessor,
    ): ResponseEntity<ExamImagePresignedUrlResponse> {
        val imagePath = examImageService.createExamImage(examId, request, accessor)
        val response = examImageService.generatePresignedUrl(imagePath)

        return ResponseEntity.ok(response)
    }

    @PutMapping("/api/v1/exams/{examId}/questions")
    fun updateQuestions(
        @PathVariable examId: Long,
        @RequestBody @Valid request: UpdateExamQuestionsWebRequest,
        @Auth accessor: Accessor,
    ): ResponseEntity<Unit> {
        val appRequest = request.toAppRequest(examId, accessor)
        examService.updateQuestions(appRequest)

        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/api/v1/exams/{examId}/title")
    fun updateTitle(
        @PathVariable examId: Long,
        @RequestBody @Valid request: UpdateExamTitleWebRequest,
        @Auth accessor: Accessor,
    ): ResponseEntity<Unit> {
        val appRequest = request.toAppRequest(examId, accessor)
        examService.updateTitle(appRequest)

        return ResponseEntity.ok().build()
    }

    @PatchMapping("/api/v1/exams/{examId}/description")
    fun updateDescription(
        @PathVariable examId: Long,
        @RequestBody @Valid request: UpdateExamDescriptionWebRequest,
        @Auth accessor: Accessor,
    ): ResponseEntity<Unit> {
        examService.updateDescription(request.toAppRequest(examId, accessor))

        return ResponseEntity.ok().build()
    }
}