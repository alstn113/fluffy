package com.pass.form.command.application;

import com.pass.form.command.application.dto.CreateFormResponse;
import com.pass.form.command.domain.Form;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FormMapper {

    private final QuestionMapper questionMapper;

    public CreateFormResponse toResponse(Form form) {
        return new CreateFormResponse(form.getId(), form.getTitle());
    }
}
