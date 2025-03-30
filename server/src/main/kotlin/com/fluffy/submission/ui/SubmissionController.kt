package com.fluffy.submission.ui

import com.fluffy.global.web.Accessor
import com.fluffy.global.web.Auth
import com.fluffy.submission.application.SubmissionQueryService
import com.fluffy.submission.application.SubmissionService
import com.fluffy.submission.application.response.SubmissionDetailResponse
import com.fluffy.submission.domain.dto.MySubmissionSummaryDto
import com.fluffy.submission.domain.dto.SubmissionSummaryDto
import com.fluffy.submission.ui.request.SubmissionWebRequest
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class SubmissionController(
    private val submissionService: SubmissionService,
    private val submissionQueryService: SubmissionQueryService
) {

    @GetMapping("/api/v1/exams/{examId}/submissions")
    fun getSummaries(
        @PathVariable examId: Long,
        @Auth accessor: Accessor
    ): ResponseEntity<List<SubmissionSummaryDto>> {
        val response = submissionQueryService.getSummariesByExamId(examId, accessor)

        return ResponseEntity.ok(response)
    }

    @GetMapping("/api/v1/exams/{examId}/submissions/{submissionId}")
    fun getDetail(
        @PathVariable examId: Long,
        @PathVariable submissionId: Long,
        @Auth accessor: Accessor
    ): ResponseEntity<SubmissionDetailResponse> {
        val response = submissionQueryService.getDetail(examId, submissionId, accessor)

        return ResponseEntity.ok(response)
    }

    @GetMapping("/api/v1/exams/{examId}/submissions/me")
    fun getMySubmissionSummaries(
        @PathVariable examId: Long,
        @Auth accessor: Accessor
    ): ResponseEntity<List<MySubmissionSummaryDto>> {
        val response = submissionQueryService.getMySubmissionSummaries(examId, accessor)

        return ResponseEntity.ok(response)
    }

    @PostMapping("/api/v1/exams/{examId}/submissions")
    fun submit(
        @PathVariable examId: Long,
        @RequestBody @Valid request: SubmissionWebRequest,
        @Auth accessor: Accessor
    ): ResponseEntity<Unit> {
        val appRequest = request.toAppRequest(examId, accessor)

        submissionService.submit(appRequest)

        return ResponseEntity.ok().build()
    }
}
