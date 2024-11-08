package com.pass.form.query.application;

import com.pass.form.query.dao.FormDataDao;
import com.pass.form.query.dto.FormData;
import com.pass.form.query.dto.FormResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FormQueryService {

    private final FormDataDao formDataDao;
    private final FormDataMapper formDataMapper;

    @Transactional(readOnly = true)
    public FormResponse getForm(String formId) {
        FormData formData = formDataDao.findById(formId)
                .orElseThrow(() -> new IllegalArgumentException("Form not found id:%s".formatted(formId)));

        return formDataMapper.toResponse(formData);
    }
}
