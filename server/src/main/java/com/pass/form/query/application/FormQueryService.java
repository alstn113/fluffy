package com.pass.form.query.application;

import com.pass.form.query.dao.FormDataDao;
import com.pass.form.query.dto.FormData;
import com.pass.form.query.dto.FormDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FormQueryService {

    private final FormDataDao formDataDao;
    private final FormDataMapper formDataMapper;

    @Transactional(readOnly = true)
    public FormDataResponse getForm(String formId) {
        FormData formData = formDataDao.getFormById(formId);

        return formDataMapper.toResponse(formData);
    }
}
