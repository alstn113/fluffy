package com.pass.submission.ui;

import com.pass.global.web.Accessor;
import com.pass.global.web.Auth;
import com.pass.submission.command.application.SubmissionService;
import com.pass.submission.ui.dto.SubmissionWebRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SubmissionController {

    private final SubmissionService submissionService;

    @PostMapping("/api/v1/exams/{examId}/submissions")
    public ResponseEntity<Void> submit(
            @PathVariable Long examId,
            @RequestBody @Valid SubmissionWebRequest request,
            @Auth Accessor accessor
    ) {
        submissionService.submit(request.toAppRequest(examId, accessor));

        return ResponseEntity.ok().build();
    }
}
