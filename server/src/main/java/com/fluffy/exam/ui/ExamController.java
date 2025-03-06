package com.fluffy.exam.ui;

import com.fluffy.exam.application.ExamImageService;
import com.fluffy.exam.application.ExamQueryService;
import com.fluffy.exam.application.ExamService;
import com.fluffy.exam.application.request.ExamImagePresignedUrlRequest;
import com.fluffy.exam.application.response.CreateExamResponse;
import com.fluffy.exam.application.response.ExamDetailResponse;
import com.fluffy.exam.application.response.ExamDetailSummaryResponse;
import com.fluffy.exam.application.response.ExamImagePresignedUrlResponse;
import com.fluffy.exam.application.response.ExamWithAnswersResponse;
import com.fluffy.exam.domain.ExamStatus;
import com.fluffy.exam.domain.dto.ExamSummaryDto;
import com.fluffy.exam.domain.dto.MyExamSummaryDto;
import com.fluffy.exam.domain.dto.SubmittedExamSummaryDto;
import com.fluffy.exam.ui.request.CreateExamWebRequest;
import com.fluffy.exam.ui.request.PublishExamWebRequest;
import com.fluffy.exam.ui.request.UpdateExamDescriptionWebRequest;
import com.fluffy.exam.ui.request.UpdateExamQuestionsWebRequest;
import com.fluffy.exam.ui.request.UpdateExamTitleWebRequest;
import com.fluffy.global.response.PageResponse;
import com.fluffy.global.web.Accessor;
import com.fluffy.global.web.Auth;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;
    private final ExamImageService examImageService;
    private final ExamQueryService examQueryService;

    @GetMapping("/api/v1/exams")
    public ResponseEntity<PageResponse<ExamSummaryDto>> getPublishedExamSummaries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<ExamSummaryDto> response = examQueryService.getPublishedExamSummaries(pageable);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/v1/exams/mine")
    public ResponseEntity<PageResponse<MyExamSummaryDto>> getMyExamSummaries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(value = "status", defaultValue = "draft") ExamStatus status,
            @Auth Accessor accessor
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<MyExamSummaryDto> response = examQueryService.getMyExamSummaries(pageable, status, accessor);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/v1/exams/{examId}/summary")
    public ResponseEntity<ExamDetailSummaryResponse> getExamDetailSummary(
            @PathVariable Long examId,
            @Auth(required = false) Accessor accessor
    ) {
        ExamDetailSummaryResponse response = examQueryService.getExamDetailSummary(examId, accessor);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/v1/exams/{examId}")
    public ResponseEntity<ExamDetailResponse> getExamDetail(@PathVariable Long examId) {
        ExamDetailResponse response = examQueryService.getExamDetail(examId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/v1/exams/{examId}/with-answers")
    public ResponseEntity<ExamWithAnswersResponse> getExamWithAnswers(
            @PathVariable Long examId,
            @Auth Accessor accessor
    ) {
        ExamWithAnswersResponse response = examQueryService.getExamWithAnswers(examId, accessor);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/v1/exams/submitted")
    public ResponseEntity<PageResponse<SubmittedExamSummaryDto>> getSubmittedExamSummaries(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @Auth Accessor accessor
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<SubmittedExamSummaryDto> response = examQueryService.getSubmittedExamSummaries(pageable, accessor);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/v1/exams")
    public ResponseEntity<CreateExamResponse> create(
            @RequestBody @Valid CreateExamWebRequest request,
            @Auth Accessor accessor
    ) {
        CreateExamResponse response = examService.create(request.toAppRequest(accessor));

        URI location = URI.create("/api/v1/exams/%s".formatted(response.id()));
        return ResponseEntity.created(location).body(response);
    }

    @PostMapping("/api/v1/exams/{examId}/publish")
    public ResponseEntity<Void> publish(
            @PathVariable Long examId,
            @RequestBody @Valid PublishExamWebRequest request,
            @Auth Accessor accessor
    ) {
        examService.publish(request.toAppRequest(examId, accessor));

        return ResponseEntity.ok().build();
    }


    @PostMapping("/api/v1/exams/{examId}/images/presigned-url")
    public ResponseEntity<ExamImagePresignedUrlResponse> createPresignedUrl(
            @PathVariable Long examId,
            @RequestBody ExamImagePresignedUrlRequest request,
            @Auth Accessor accessor
    ) {
        ExamImagePresignedUrlResponse response = examImageService.createPresignedUrl(examId, request, accessor);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/api/v1/exams/{examId}/questions")
    public ResponseEntity<Void> updateQuestions(
            @PathVariable Long examId,
            @RequestBody @Valid UpdateExamQuestionsWebRequest request,
            @Auth Accessor accessor
    ) {
        examService.updateQuestions(request.toAppRequest(examId, accessor));

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/api/v1/exams/{examId}/title")
    public ResponseEntity<Void> updateTitle(
            @PathVariable Long examId,
            @RequestBody @Valid UpdateExamTitleWebRequest request,
            @Auth Accessor accessor
    ) {
        examService.updateTitle(request.toAppRequest(examId, accessor));

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/api/v1/exams/{examId}/description")
    public ResponseEntity<Void> updateDescription(
            @PathVariable Long examId,
            @RequestBody @Valid UpdateExamDescriptionWebRequest request,
            @Auth Accessor accessor
    ) {
        examService.updateDescription(request.toAppRequest(examId, accessor));

        return ResponseEntity.ok().build();
    }
}
