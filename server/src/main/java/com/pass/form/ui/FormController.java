package com.pass.form.ui;

import com.pass.form.command.application.FormService;
import com.pass.form.command.application.dto.CreateFormResponse;
import com.pass.form.query.application.FormQueryService;
import com.pass.form.query.dto.FormDataResponse;
import com.pass.form.ui.dto.CreateFormWebRequest;
import com.pass.form.ui.dto.PublishFormWebRequest;
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
public class FormController {

    private final FormService formService;
    private final FormQueryService formQueryService;

    @GetMapping("/api/v1/forms/{formId}")
    public ResponseEntity<FormDataResponse> getForm(@PathVariable String formId) {
        FormDataResponse formDataResponse = formQueryService.getForm(formId);

        return ResponseEntity.ok(formDataResponse);
    }

    @PostMapping("/api/v1/forms")
    public ResponseEntity<CreateFormResponse> create(
            @RequestBody @Valid CreateFormWebRequest request,
            @Auth Accessor accessor
    ) {
        CreateFormResponse response = formService.create(request.toAppRequest(accessor));

        URI location = URI.create("/api/v1/forms/%s".formatted(response.id()));
        return ResponseEntity.created(location).body(response);
    }

    @PostMapping("/api/v1/forms/{formId}/publish")
    public ResponseEntity<Void> publish(
            @PathVariable String formId,
            @RequestBody @Valid PublishFormWebRequest request,
            @Auth Accessor accessor
    ) {
        formService.publish(request.toAppRequest(formId, accessor));

        return ResponseEntity.noContent().build();
    }
}
