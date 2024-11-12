package com.pass.exam.ui;

import com.pass.exam.command.application.ExamService;
import com.pass.exam.command.application.dto.CreateExamResponse;
import com.pass.exam.query.application.ExamQueryService;
import com.pass.exam.query.dto.ExamDataResponse;
import com.pass.exam.ui.dto.CreateExamWebRequest;
import com.pass.exam.ui.dto.PublishExamWebRequest;
import com.pass.global.web.Accessor;
import com.pass.global.web.Auth;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;
    private final ExamQueryService examQueryService;

    // published exam 리스트 조회

    // my exam 리스트 조회

    @GetMapping("/api/v1/exams/{examId}")
    public ResponseEntity<ExamDataResponse> getExam(@PathVariable String examId) {
        ExamDataResponse examDataResponse = examQueryService.getExam(examId);

        return ResponseEntity.ok(examDataResponse);
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
            @PathVariable String examId,
            @RequestBody @Valid PublishExamWebRequest request,
            @Auth Accessor accessor
    ) {
        examService.publish(request.toAppRequest(examId, accessor));

        return ResponseEntity.noContent().build();
    }
}
