package com.fluffy.comment.api;

import com.fluffy.comment.api.dto.CreateExamCommentWebRequest;
import com.fluffy.comment.api.dto.DeleteExamCommentWebResponse;
import com.fluffy.comment.application.ExamCommentService;
import com.fluffy.comment.application.dto.CreateExamCommentRequest;
import com.fluffy.comment.application.dto.CreateExamCommentResponse;
import com.fluffy.comment.application.dto.DeleteExamCommentRequest;
import com.fluffy.global.web.Accessor;
import com.fluffy.global.web.Auth;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExamCommentController {

    private final ExamCommentService examCommentService;

    @PostMapping("/api/v1/exams/{examId}/comments")
    public ResponseEntity<CreateExamCommentResponse> createComment(
            @PathVariable Long examId,
            @RequestBody @Valid CreateExamCommentWebRequest request,
            @Auth Accessor accessor
    ) {
        CreateExamCommentRequest appRequest = request.toAppRequest(examId, accessor.id());
        CreateExamCommentResponse response = examCommentService.createComment(appRequest);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/api/v1/comments/{commentId}")
    public ResponseEntity<DeleteExamCommentWebResponse> deleteComment(
            @PathVariable Long commentId,
            @Auth Accessor accessor
    ) {
        DeleteExamCommentRequest request = new DeleteExamCommentRequest(commentId, accessor.id());
        Long deletedCommentId = examCommentService.deleteComment(request);

        return ResponseEntity.ok(new DeleteExamCommentWebResponse(deletedCommentId));
    }
}
