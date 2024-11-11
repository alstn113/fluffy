package com.pass.submission.ui;

import com.pass.submission.ui.dto.SubmitWebRequest;
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

    @PostMapping("/api/v1/exams/{examId}/submissions")
    public ResponseEntity<Void> submit(
            @PathVariable Long examId,
            @RequestBody @Valid SubmitWebRequest request
    ) {
        return ResponseEntity.ok().build();
    }
}
