package com.pass.exam.ui;

import com.pass.exam.application.ExamQueryService;
import com.pass.exam.application.ExamService;
import com.pass.exam.application.dto.ExamResponse;
import com.pass.exam.application.dto.ExamWithAnswersResponse;
import com.pass.exam.application.dto.question.CreateExamResponse;
import com.pass.exam.domain.ExamStatus;
import com.pass.exam.domain.dto.ExamSummaryDto;
import com.pass.exam.ui.dto.CreateExamWebRequest;
import com.pass.exam.ui.dto.PublishExamWebRequest;
import com.pass.exam.ui.dto.UpdateExamInfoWebRequest;
import com.pass.exam.ui.dto.UpdateExamQuestionsWebRequest;
import com.pass.global.web.Accessor;
import com.pass.global.web.Auth;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    private final ExamQueryService examQueryService;

    @GetMapping("/api/v1/exams")
    public ResponseEntity<List<ExamSummaryDto>> getPublishedExamSummaries() {
        List<ExamSummaryDto> response = examQueryService.getPublishedExamSummaries();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/v1/exams/mine")
    public ResponseEntity<List<ExamSummaryDto>> getMyExamSummaries(
            @RequestParam(value = "status", defaultValue = "draft") ExamStatus status,
            @Auth Accessor accessor
    ) {
        List<ExamSummaryDto> response = examQueryService.getMyExamSummaries(status, accessor);

        return ResponseEntity.ok(response);
    }


    @GetMapping("/api/v1/exams/{examId}")
    public ResponseEntity<ExamResponse> getExam(@PathVariable Long examId) {
        ExamResponse response = examQueryService.getExam(examId);

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

    @PutMapping("/api/v1/exams/{examId}/questions")
    public ResponseEntity<Void> updateQuestions(
            @PathVariable Long examId,
            @RequestBody @Valid UpdateExamQuestionsWebRequest request,
            @Auth Accessor accessor
    ) {
        examService.updateQuestions(request.toAppRequest(examId, accessor));

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/api/v1/exams/{examId}/info")
    public ResponseEntity<Void> updateInfo(
            @PathVariable Long examId,
            @RequestBody @Valid UpdateExamInfoWebRequest request,
            @Auth Accessor accessor
    ) {
        examService.updateInfo(request.toAppRequest(examId, accessor));

        return ResponseEntity.ok().build();
    }
}
