package com.fluffy.comment.ui;

import com.fluffy.comment.application.ExamCommentQueryService;
import com.fluffy.comment.application.ExamCommentService;
import com.fluffy.comment.application.dto.CreateExamCommentRequest;
import com.fluffy.comment.application.dto.CreateExamCommentResponse;
import com.fluffy.comment.application.dto.DeleteExamCommentRequest;
import com.fluffy.comment.domain.dto.ExamReplyCommentDto;
import com.fluffy.comment.domain.dto.ExamRootCommentDto;
import com.fluffy.comment.ui.dto.CreateExamCommentWebRequest;
import com.fluffy.comment.ui.dto.DeleteExamCommentWebResponse;
import com.fluffy.comment.ui.dto.ExamReplyCommentsWebResponse;
import com.fluffy.comment.ui.dto.ExamRootCommentsWebResponse;
import com.fluffy.global.web.Accessor;
import com.fluffy.global.web.Auth;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExamCommentController {

    private final ExamCommentService examCommentService;
    private final ExamCommentQueryService examCommentQueryService;

    @GetMapping("/api/v1/exams/{examId}/comments")
    public ResponseEntity<ExamRootCommentsWebResponse> getRootComments(
            @PathVariable Long examId
    ) {
        List<ExamRootCommentDto> rootComments = examCommentQueryService.getRootComments(examId);

        return ResponseEntity.ok(new ExamRootCommentsWebResponse(rootComments));
    }

    @GetMapping("/api/v1/exams/comments/{commentId}/replies")
    public ResponseEntity<ExamReplyCommentsWebResponse> getReplyComments(
            @PathVariable Long commentId
    ) {
        List<ExamReplyCommentDto> replies = examCommentQueryService.getReplyComments(commentId);

        return ResponseEntity.ok(new ExamReplyCommentsWebResponse(replies));
    }

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

    @DeleteMapping("/api/v1/exams/comments/{commentId}")
    public ResponseEntity<DeleteExamCommentWebResponse> deleteComment(
            @PathVariable Long commentId,
            @Auth Accessor accessor
    ) {
        DeleteExamCommentRequest request = new DeleteExamCommentRequest(commentId, accessor.id());
        Long deletedCommentId = examCommentService.deleteComment(request);

        return ResponseEntity.ok(new DeleteExamCommentWebResponse(deletedCommentId));
    }
}
