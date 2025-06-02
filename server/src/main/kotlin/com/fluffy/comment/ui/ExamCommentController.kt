package com.fluffy.comment.ui

import com.fluffy.comment.application.ExamCommentQueryService
import com.fluffy.comment.application.ExamCommentService
import com.fluffy.comment.application.request.DeleteExamCommentRequest
import com.fluffy.comment.application.response.CreateExamCommentResponse
import com.fluffy.comment.ui.request.CreateExamCommentWebRequest
import com.fluffy.comment.ui.response.DeleteExamCommentWebResponse
import com.fluffy.comment.ui.response.ExamReplyCommentsWebResponse
import com.fluffy.comment.ui.response.ExamRootCommentsWebResponse
import com.fluffy.global.web.Accessor
import com.fluffy.global.web.Auth
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ExamCommentController(
    private val examCommentService: ExamCommentService,
    private val examCommentQueryService: ExamCommentQueryService,
) {

    @GetMapping("/api/v1/exams/{examId}/comments")
    fun getRootComments(
        @PathVariable examId: Long,
    ): ResponseEntity<ExamRootCommentsWebResponse> {
        val rootComments = examCommentQueryService.getRootComments(examId)
        val response = ExamRootCommentsWebResponse(rootComments)

        return ResponseEntity.ok(response)
    }

    @GetMapping("/api/v1/exams/comments/{commentId}/replies")
    fun getReplyComments(
        @PathVariable commentId: Long,
    ): ResponseEntity<ExamReplyCommentsWebResponse> {
        val replies = examCommentQueryService.getReplyComments(commentId)
        val response = ExamReplyCommentsWebResponse(replies)

        return ResponseEntity.ok(response)
    }

    @PostMapping("/api/v1/exams/{examId}/comments")
    fun createComment(
        @PathVariable examId: Long,
        @RequestBody @Valid request: CreateExamCommentWebRequest,
        @Auth accessor: Accessor,
    ): ResponseEntity<CreateExamCommentResponse> {
        val appRequest = request.toAppRequest(examId, accessor.id)
        val response = examCommentService.createComment(appRequest)

        return ResponseEntity.ok(response)
    }

    @DeleteMapping("/api/v1/exams/comments/{commentId}")
    fun deleteComment(
        @PathVariable commentId: Long,
        @Auth accessor: Accessor,
    ): ResponseEntity<DeleteExamCommentWebResponse> {
        val appRequest = DeleteExamCommentRequest(commentId, accessor.id)
        val deletedCommentId = examCommentService.deleteComment(appRequest)

        val response = DeleteExamCommentWebResponse(deletedCommentId)

        return ResponseEntity.ok(response)
    }
}