package com.pass.form.query.application;

import com.pass.form.query.dto.FormData;
import com.pass.form.query.dto.FormResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FormDataMapper {

    private final QuestionDataMapper questionDataMapper;

    public FormResponse toResponse(FormData formData) {
        return new FormResponse(
                formData.getId(),
                formData.getTitle(),
                formData.getDescription(),
                questionDataMapper.toResponses(formData.getQuestions()),
                formData.getCreatedAt(),
                formData.getUpdatedAt()
        );
    }
}
