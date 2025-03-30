package com.fluffy.exam.ui

import com.fluffy.exam.application.ExamLikeService
import com.fluffy.global.web.Accessor
import com.fluffy.global.web.Auth
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ExamLikeController(
    private val examLikeService: ExamLikeService
) {

    @PostMapping("/api/v1/exams/{examId}/like")
    fun like(
        @PathVariable examId: Long,
        @Auth accessor: Accessor
    ): ResponseEntity<Unit> {
        examLikeService.like(examId, accessor)

        return ResponseEntity.ok().build()
    }

    @DeleteMapping("/api/v1/exams/{examId}/like")
    fun unlike(
        @PathVariable examId: Long,
        @Auth accessor: Accessor
    ): ResponseEntity<Unit> {
        examLikeService.unlike(examId, accessor)

        return ResponseEntity.ok().build()
    }
}