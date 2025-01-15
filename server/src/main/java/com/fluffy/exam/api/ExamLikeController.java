package com.fluffy.exam.api;

import com.fluffy.exam.application.ExamLikeService;
import com.fluffy.global.web.Accessor;
import com.fluffy.global.web.Auth;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExamLikeController {

    private final ExamLikeService examLikeService;

    @PostMapping("/api/v1/exams/{examId}/like")
    public ResponseEntity<Void> like(
            @PathVariable Long examId,
            @Auth Accessor accessor
    ) {
        examLikeService.like(examId, accessor);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/v1/exams/{examId}/like")
    public ResponseEntity<Void> unlike(
            @PathVariable Long examId,
            @Auth Accessor accessor
    ) {
        examLikeService.unlike(examId, accessor);

        return ResponseEntity.ok().build();
    }
}
