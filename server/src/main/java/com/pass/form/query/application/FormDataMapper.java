package com.pass.form.query.application;

import com.pass.form.query.dto.FormData;
import com.pass.form.query.dto.FormDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FormDataMapper {

    private final QuestionDataMapper questionDataMapper;

    public FormDataResponse toResponse(FormData formData) {
        return new FormDataResponse(
                formData.getId(),
                formData.getTitle(),
                formData.getDescription(),
                questionDataMapper.toResponses(formData.getQuestions()),
                formData.getCreatedAt(),
                formData.getUpdatedAt()
        );
    }
}
