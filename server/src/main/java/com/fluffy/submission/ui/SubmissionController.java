package com.fluffy.submission.ui;

import com.fluffy.global.web.Accessor;
import com.fluffy.global.web.Auth;
import com.fluffy.submission.application.SubmissionQueryService;
import com.fluffy.submission.application.SubmissionService;
import com.fluffy.submission.application.response.SubmissionDetailResponse;
import com.fluffy.submission.domain.dto.MySubmissionSummaryDto;
import com.fluffy.submission.domain.dto.SubmissionSummaryDto;
import com.fluffy.submission.ui.request.SubmissionWebRequest;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SubmissionController {

    private final SubmissionService submissionService;
    private final SubmissionQueryService submissionQueryService;

    @GetMapping("/api/v1/exams/{examId}/submissions")
    public ResponseEntity<List<SubmissionSummaryDto>> getSummaries(
            @PathVariable Long examId,
            @Auth Accessor accessor
    ) {
        List<SubmissionSummaryDto> response = submissionQueryService.getSummariesByExamId(examId, accessor);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/v1/exams/{examId}/submissions/{submissionId}")
    public ResponseEntity<SubmissionDetailResponse> getDetail(
            @PathVariable Long examId,
            @PathVariable Long submissionId,
            @Auth Accessor accessor
    ) {
        SubmissionDetailResponse response = submissionQueryService.getDetail(examId, submissionId, accessor);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/v1/exams/{examId}/submissions/me")
    public ResponseEntity<List<MySubmissionSummaryDto>> getMySubmissionSummaries(
            @PathVariable Long examId,
            @Auth Accessor accessor
    ) {
        List<MySubmissionSummaryDto> response = submissionQueryService.getMySubmissionSummaries(examId, accessor);

        return ResponseEntity.ok(response);
    }


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
